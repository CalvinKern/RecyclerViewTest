package com.pixio.recyclertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the recycler view and set up the layout manager as well as the adapter
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);

        if (recycler != null) {
            recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recycler.setAdapter(new MainRecyclerAdapter());
        }
    }

    public void onFabClicked(View v) {
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);

        if (recycler != null) {
            MainRecyclerAdapter adapter = (MainRecyclerAdapter) recycler.getAdapter();
            if (adapter != null) {
                adapter.addInfoString("Test Info");
            }
        }
    }

    public interface OnViewHolderClickedListener {
        void onViewHolderClicked(int position);
    }

    public class MainRecyclerAdapter extends RecyclerView.Adapter implements OnViewHolderClickedListener {

        private static final int HEADER_TYPE = 0;
        private static final int CONTACT_TYPE = 1;
        private static final int INFO_TYPE = 2;
        private static final int FOOTER_TYPE = 3;

        List<String> mInfoStrings;

        public MainRecyclerAdapter() {
            mInfoStrings = new ArrayList<>();
        }

        @Override
        public int getItemViewType(int position) {

            // Get the footer position (the last item in the list)
            int footerPosition = getItemCount() - 1;

            if (position == 0) {
                return HEADER_TYPE;
            } else if (position == 1) {
                return CONTACT_TYPE;
            } else if (position == footerPosition) {
                return FOOTER_TYPE;
            } else {
                return INFO_TYPE;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            RecyclerView.ViewHolder holder; // Declare the ViewHolder to initialize later
            switch (viewType) {
                case HEADER_TYPE: {
                    holder = new HeaderViewHolder(inflater.inflate(R.layout.holder_header, parent, false));
                    break;
                }
                case CONTACT_TYPE: {
                    holder = new ContactViewHolder(inflater.inflate(R.layout.holder_contact, parent, false));
                    break;
                }
                case FOOTER_TYPE: {
                    holder = new FooterViewHolder(inflater.inflate(R.layout.holder_footer, parent, false));
                    break;
                }
                case INFO_TYPE:
                    // Intentional fallthrough
                default: {
                    holder = new InfoViewHolder(inflater.inflate(R.layout.holder_info, parent, false), this);
                    break;
                }
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case HEADER_TYPE: {
                    ((HeaderViewHolder) holder).mHeaderTextView.setText("This is a title!");
                    break;
                }
                case CONTACT_TYPE: {
                    ((ContactViewHolder) holder).mContactNameView.setText("Person Name");
                    ((ContactViewHolder) holder).mContactImageView.setImageResource(R.drawable.background);
                    break;
                }
                case INFO_TYPE: {
                    ((InfoViewHolder) holder).mInfoTextView.setText(mInfoStrings.get(position - 2));
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return 3 + mInfoStrings.size(); // 3 = Header + Contact + Footer
        }

        public void addInfoString(String info) {
            mInfoStrings.add(info);
            // Notify an insert of the item into the end of the info section
            notifyItemInserted(getItemCount() - 2);
        }

        @Override
        public void onViewHolderClicked(int position) {
            mInfoStrings.remove(position - 2);
            notifyItemRemoved(position);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView mHeaderTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            mHeaderTextView = (TextView) itemView.findViewById(R.id.holder_header_text_view);
        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView mContactNameView;
        public ImageView mContactImageView;

        public ContactViewHolder(View itemView) {
            super(itemView);

            mContactNameView = (TextView) itemView.findViewById(R.id.holder_contact_name_view);
            mContactImageView = (ImageView) itemView.findViewById(R.id.holder_contact_image_view);
        }
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mDeleteView;
        public TextView mInfoTextView;

        private WeakReference<OnViewHolderClickedListener> mClickListener;

        public InfoViewHolder(View itemView, OnViewHolderClickedListener listener) {
            super(itemView);

            mDeleteView = itemView.findViewById(R.id.holder_info_delete);
            mInfoTextView = (TextView) itemView.findViewById(R.id.holder_info_text_view);

            mDeleteView.setOnClickListener(this);

            mClickListener = new WeakReference<>(listener);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener.get() != null) {
                mClickListener.get().onViewHolderClicked(getAdapterPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
