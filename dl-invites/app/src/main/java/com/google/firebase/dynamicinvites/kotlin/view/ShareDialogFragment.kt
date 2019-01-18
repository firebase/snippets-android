package com.google.firebase.dynamicinvites.kotlin.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent
import com.google.firebase.dynamicinvites.presenter.CopyPresenter
import com.google.firebase.dynamicinvites.presenter.EmailPresenter
import com.google.firebase.dynamicinvites.presenter.InvitePresenter
import com.google.firebase.dynamicinvites.presenter.MessagePresenter
import com.google.firebase.dynamicinvites.presenter.MorePresenter
import com.google.firebase.dynamicinvites.presenter.SocialPresenter
import com.google.firebase.dynamicinvites.util.DynamicLinksUtil

import java.util.Arrays

/**
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 *
 * You can show this modal bottom sheet from your activity like this:
 *
 * <pre>
 * ShareDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
</pre> *
 *
 *
 * You activity (or fragment) needs to implement [ShareDialogFragment.Listener].
 */
class ShareDialogFragment : BottomSheetDialogFragment() {

    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val content = DynamicLinksUtil.generateInviteContent()
        val presenters = Arrays.asList(
                EmailPresenter(true, content),
                SocialPresenter(true, content),
                MessagePresenter(true, content),
                CopyPresenter(true, content),
                MorePresenter(true, content)
        )

        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ItemAdapter(presenters)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            mListener = parent as Listener
        } else {
            mListener = context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onItemClicked(presenter: InvitePresenter)
    }

    private inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_share_method, parent, false)) {

        private val text: TextView
        private val icon: ImageView

        private lateinit var presenter: InvitePresenter

        init {

            text = itemView.findViewById(R.id.item_name)
            icon = itemView.findViewById(R.id.item_icon)

            itemView.setOnClickListener {
                mListener?.onItemClicked(presenter)
                dismiss()
            }
        }

        internal fun bind(presenter: InvitePresenter) {
            this.presenter = presenter

            text.text = presenter.name
            icon.setImageResource(presenter.icon)
        }
    }

    private inner class ItemAdapter(private val items: List<InvitePresenter>)
        : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val presenter = items[position]
            holder.bind(presenter)
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    companion object {

        fun newInstance(): ShareDialogFragment {
            val fragment = ShareDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
