package com.google.firebase.dynamicinvites.view;

import com.google.firebase.dynamicinvites.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.dynamicinvites.presenter.InvitePresenter;
import com.google.firebase.dynamicinvites.presenter.CopyPresenter;
import com.google.firebase.dynamicinvites.presenter.EmailPresenter;
import com.google.firebase.dynamicinvites.presenter.MessagePresenter;
import com.google.firebase.dynamicinvites.presenter.MorePresenter;
import com.google.firebase.dynamicinvites.presenter.SocialPresenter;

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
        final ShareDialogFragment fragment = new ShareDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        // TODO: Non-null content
        List<InvitePresenter> presenters = Arrays.asList(
                new EmailPresenter(true, null),
                new SocialPresenter(true, null),
                new MessagePresenter(true, null),
                new CopyPresenter(true, null),
                new MorePresenter(true, null)
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

            text = itemView.findViewById(R.id.item_name);
            icon = itemView.findViewById(R.id.item_icon);

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
