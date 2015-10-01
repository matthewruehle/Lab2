package com.mtruehle.photostreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
    private WebView feedView;
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
        feedView = (WebView) feedLayout.findViewById(R.id.feed_webView);
        helper = new FeedDbHelper(getContext());
        updateSavedUrls();
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
        return feedLayout;

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
            feedView.loadUrl(urlToShow);
            Log.i("PRINTER", urlToShow);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("PRINTER", "WebView failed...");
        }
    }

    public void goToPrevious() {
        if (currentlyDisplayedImage != -1) {
            currentlyDisplayedImage = currentlyDisplayedImage - 1;
            if (currentlyDisplayedImage < 0) {
                currentlyDisplayedImage = savedUrls.size() - 1;
            }
            updateWebView();
        }
    }

    public void goToNext() {
        if (currentlyDisplayedImage != -1) {
            currentlyDisplayedImage = currentlyDisplayedImage + 1;
            if (currentlyDisplayedImage >= savedUrls.size()) {
                currentlyDisplayedImage = 0;
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
