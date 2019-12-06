package com.google.firebase.dynamicinvites.kotlin.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.dynamicinvites.R
import com.google.firebase.dynamicinvites.kotlin.presenter.CopyPresenter
import com.google.firebase.dynamicinvites.kotlin.presenter.EmailPresenter
import com.google.firebase.dynamicinvites.kotlin.presenter.InvitePresenter
import com.google.firebase.dynamicinvites.kotlin.presenter.MessagePresenter
import com.google.firebase.dynamicinvites.kotlin.presenter.MorePresenter
import com.google.firebase.dynamicinvites.kotlin.presenter.SocialPresenter
import com.google.firebase.dynamicinvites.kotlin.util.DynamicLinksUtil
import kotlinx.android.synthetic.main.fragment_item_list_dialog.recycler
import kotlinx.android.synthetic.main.item_share_method.view.itemIcon
import kotlinx.android.synthetic.main.item_share_method.view.itemName

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

    private var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val content = DynamicLinksUtil.generateInviteContent()
        val presenters = listOf(
                EmailPresenter(true, content),
                SocialPresenter(true, content),
                MessagePresenter(true, content),
                CopyPresenter(true, content),
                MorePresenter(true, content)
        )

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = ItemAdapter(presenters)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        listener = if (parent != null) {
            parent as Listener
        } else {
            context as Listener
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface Listener {
        fun onItemClicked(presenter: InvitePresenter)
    }

    private inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_share_method, parent, false)) {

        internal fun bind(presenter: InvitePresenter) {
            itemView.itemName.text = presenter.name
            itemView.itemIcon.setImageResource(presenter.icon)

            itemView.setOnClickListener {
                listener?.onItemClicked(presenter)
                dismiss()
            }
        }
    }

    private inner class ItemAdapter(private val items: List<InvitePresenter>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val presenter = items[position]
            holder.bind(presenter)
        }

        override fun getItemCount() = items.size
    }

    companion object {
        fun newInstance(): ShareDialogFragment {
            return ShareDialogFragment()
        }
    }
}
