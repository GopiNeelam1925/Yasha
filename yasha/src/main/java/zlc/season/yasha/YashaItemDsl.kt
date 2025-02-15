package zlc.season.yasha

import android.view.LayoutInflater
import android.view.ViewGroup

class YashaItemDsl<T> {
    private var resId: Int = 0
    private var onBind: YashaScope.(t: T) -> Unit = {}
    private var onBindPayload: YashaScope.(t: T, payload: List<Any>) -> Unit = { _: T, _: List<Any> -> }

    private var onAttach: YashaScope.(t: T) -> Unit = {}
    private var onDetach: YashaScope.(t: T) -> Unit = {}
    private var onRecycled: YashaScope.(t: T) -> Unit = {}

    private var gridSpanSize = 1
    private var staggerFullSpan = false

    /**
     * Set item res layout resource
     */
    fun res(res: Int) {
        this.resId = res
    }

    fun onBind(block: YashaScope.(t: T) -> Unit) {
        this.onBind = block
    }

    fun onBindPayload(block: YashaScope.(t: T, payload: List<Any>) -> Unit) {
        this.onBindPayload = block
    }

    fun onAttach(block: YashaScope.(t: T) -> Unit) {
        this.onAttach = block
    }

    fun onDetach(block: YashaScope.(t: T) -> Unit) {
        this.onDetach = block
    }

    fun onRecycled(block: YashaScope.(t: T) -> Unit) {
        this.onRecycled = block
    }

    /**
     * Only work for Grid, set the span size of this item
     *
     * @param spanSize spanSize
     */
    fun gridSpanSize(spanSize: Int) {
        this.gridSpanSize = spanSize
    }

    /**
     * Only work for Stagger, set the fullSpan of this item
     *
     * @param fullSpan True or false
     */
    fun staggerFullSpan(fullSpan: Boolean) {
        this.staggerFullSpan = fullSpan
    }

    fun prepare(key: Int, adapter: YashaAdapter) {
        adapter.setItemBuilder(key, generateItemBuilder())
    }

    private fun generateItemBuilder(): YashaItemBuilder {
        return YashaItemBuilder(
                gridSpanSize,
                staggerFullSpan,
                ::builder
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun builder(viewGroup: ViewGroup): YashaViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(this.resId, viewGroup, false)

        return object : YashaViewHolder(view) {
            var viewHolderScope = YashaScope(view)

            override fun onBind(t: YashaItem) {
                t as T
                viewHolderScope.onBind(t)
            }

            override fun onBindPayload(t: YashaItem, payload: MutableList<Any>) {
                t as T
                viewHolderScope.onBindPayload(t, payload)
            }

            override fun onAttach(t: YashaItem) {
                t as T
                viewHolderScope.onAttach(t)
            }

            override fun onDetach(t: YashaItem) {
                t as T
                viewHolderScope.onDetach(t)
            }

            override fun onRecycled(t: YashaItem) {
                t as T
                viewHolderScope.onRecycled(t)
            }
        }
    }
}