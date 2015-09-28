package com.mtruehle.photostreamer;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    private FeedDbHelper helper;
    private WebView webView;
    private int currentlyDisplayedImage;
    private ArrayList<String> savedUrls;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout feedLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_feed, container, false);
        Button backButton = (Button) feedLayout.findViewById(R.id.back_button_feed);
        Button unsaveButton = (Button) feedLayout.findViewById(R.id.unsave_button_feed);
        Button nextButton = (Button) feedLayout.findViewById(R.id.next_button_feed);
        webView = (WebView) feedLayout.findViewById(R.id.feed_webView);
        helper = new FeedDbHelper(getContext());
        currentlyDisplayedImage = -1;
        savedUrls = helper.readFeedDb();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrevious();
            }
        });
        unsaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsaveCurrent();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });
        if (savedUrls.size() != 0) {
            currentlyDisplayedImage = 0;
            updateWebView();
        }
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    public void updateSavedUrls() {
        savedUrls = helper.readFeedDb();
        if (savedUrls.size() == 0) {
            currentlyDisplayedImage = -1;
        } else {
            currentlyDisplayedImage = 0;
            updateWebView();
        }
    }

    public void updateWebView() {
        String urlToShow = savedUrls.get(currentlyDisplayedImage);
        try {
            webView.loadUrl(urlToShow);
            Log.i("PRINTER", urlToShow);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("PRINTER", "WebView failed...");
        }
    }

    public void goToPrevious() {
        if (currentlyDisplayedImage != -1) {
            currentlyDisplayedImage = currentlyDisplayedImage - 1;
            while (currentlyDisplayedImage < 0) {
                currentlyDisplayedImage = 10 + currentlyDisplayedImage;
            }
            updateWebView();
        }
    }

    public void goToNext() {
        if (currentlyDisplayedImage != -1) {
            currentlyDisplayedImage = currentlyDisplayedImage + 1;
            while (currentlyDisplayedImage >= 10) {
                currentlyDisplayedImage = currentlyDisplayedImage - 10;
            }
            updateWebView();
        }
    }

    public void unsaveCurrent() {
        if (currentlyDisplayedImage != -1) {
            String currentUrl = savedUrls.get(currentlyDisplayedImage);
            helper.deleteEntryFromFeed(currentUrl);
            Toast.makeText(getActivity(), "Unsaved.", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: Add in some sort of a resave functionality, so that if I accidentally click the unsave button I can "change my mind" easily.
    //TODO: Implement a way to go between entries more elegantly (e.g., swiping instead of buttons)
    //TODO: Auto-zooming.
    //TODO: Sharing to clipboard.

}
