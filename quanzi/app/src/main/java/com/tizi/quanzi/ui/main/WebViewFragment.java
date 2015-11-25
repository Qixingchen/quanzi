package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends BaseFragment {

    public static final int License = 1;
    public static final int Theme_Intro = 2;
    private static final String TYPE = "param1";
    private static final String URI = "param2";
    private int type;
    private String uri;
    private WebView webView;
    private Button button1;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(@TYPE_DEF int type, String uri) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putString(URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
            uri = getArguments().getString(URI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    protected void findViews(View view) {
        webView = (WebView) view.findViewById(R.id.web_view);
        button1 = (Button) view.findViewById(R.id.web_view_button1);
    }

    @Override
    protected void initViewsAndSetEvent() {
        if (type == Theme_Intro) {
            button1.setText("报名");
            button1.setVisibility(View.VISIBLE);
        }
        webView.loadUrl(uri);
    }

    @IntDef({License, Theme_Intro})
    public @interface TYPE_DEF {
    }

}
