package com.horizon.doodle.worker

import java.util.*
import java.util.concurrent.Future

/**
 * [TagExecutor]标签Executor，主要用于避免任务重复执行。
 *
 * 其原理为：
 * 1、给任务打tag，相同tag的任务视为相同的任务；
 * 2、记录进入调度的任务（委托给[executor]);
 * 3、任务提交过来，如果已经有相同的任务在进入调度，需要等待；
 *
 * 效果：
 * 1、tag相同的任务会串行，不同tag的任务会并发执行;
 * 2、可以防止重复执行,
 *    比如图片加载，当相同的源的任务同时发起，如果并行执行，则会重复加载，
 *    如果使之串行，配合缓存，第一个任务完成后，后面的任务可以直接取缓存，而避免重复加载；
 * 3、还可以当串行执行器用（tag相同串行执行，任务队列无限容量）。
 */
class TagExecutor(private val executor: PipeExecutor) : PriorityExecutor {
    private val scheduledTasks = HashMap<String, Runnable>()
    private val waitingQueues by lazy { HashMap<String, CircularQueue<TaskWrapper>>() }

    private class TaskWrapper(val r: Runnable, val priority: Int)

    private inner class LaneTrigger(val tag : String) : Trigger{
        override fun next() {
            executor.scheduleNext()
            scheduleNext(tag)
        }
    }

    private fun start(r: Runnable, tag: String, priority: Int) {
        scheduledTasks[tag] = r
        executor.schedule(RunnableWrapper(r, LaneTrigger(tag)), priority)
    }

    @Synchronized
    fun scheduleNext(tag: String) {
        scheduledTasks.remove(tag)
        waitingQueues[tag]?.let { queue ->
            val wrapper = queue.poll()
            if (wrapper == null) {
                // 如果队列清空了，则顺便把队列从HashMap移除，不然HashMap只增不减，浪费内存
                waitingQueues.remove(tag)
            } else {
                start(wrapper.r, tag, wrapper.priority)
            }
        }
    }

    override fun execute(r: Runnable) {
        executor.execute(r)
    }

    @Synchronized
    fun execute(tag: String, r: Runnable, priority: Int = Priority.NORMAL) {
        if (tag.isEmpty()) {
            executor.execute(r, priority)
        } else if (!scheduledTasks.containsKey(tag)) {
            start(r, tag, priority)
        } else {
            val queue = waitingQueues[tag] ?: CircularQueue<TaskWrapper>().apply { waitingQueues[tag] = this }
            queue.offer(TaskWrapper(r, priority))
        }
    }

    override fun changePriority(r: Runnable, priority: Int, increment: Int): Int {
        return executor.changePriority(r, priority, increment)
    }
}