package com.google.firebase.dynamicinvites.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.kotlin.model.InviteContent;
import com.google.firebase.dynamicinvites.presenter.CopyPresenter;
import com.google.firebase.dynamicinvites.presenter.EmailPresenter;
import com.google.firebase.dynamicinvites.presenter.InvitePresenter;
import com.google.firebase.dynamicinvites.presenter.MessagePresenter;
import com.google.firebase.dynamicinvites.presenter.MorePresenter;
import com.google.firebase.dynamicinvites.presenter.SocialPresenter;
import com.google.firebase.dynamicinvites.util.DynamicLinksUtil;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * <p>You can show this modal bottom sheet from your activity like this:
 *
 * <pre>
 *     ShareDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
 * </pre>
 *
 * <p>You activity (or fragment) needs to implement {@link ShareDialogFragment.Listener}.
 */
public class ShareDialogFragment extends BottomSheetDialogFragment {

    private Listener mListener;

    public static ShareDialogFragment newInstance() {
        return new ShareDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        InviteContent content = DynamicLinksUtil.generateInviteContent();
        List<InvitePresenter> presenters = Arrays.asList(
                new EmailPresenter(true, content),
                new SocialPresenter(true, content),
                new MessagePresenter(true, content),
                new CopyPresenter(true, content),
                new MorePresenter(true, content)
        );

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(presenters));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(InvitePresenter presenter);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private final ImageView icon;

        private InvitePresenter presenter;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_share_method, parent, false));

            text = itemView.findViewById(R.id.itemName);
            icon = itemView.findViewById(R.id.itemIcon);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.onItemClicked(presenter);
                                dismiss();
                            }
                        }
                    });
        }

        void bind(InvitePresenter presenter) {
            this.presenter = presenter;

            text.setText(presenter.name);
            icon.setImageResource(presenter.icon);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<InvitePresenter> items;

        ItemAdapter(List<InvitePresenter> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            InvitePresenter presenter = items.get(position);
            holder.bind(presenter);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
