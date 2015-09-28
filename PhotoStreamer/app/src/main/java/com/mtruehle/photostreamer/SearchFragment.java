package com.mtruehle.photostreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchFragment extends Fragment {
    private HttpHandler handler;
    private FeedDbHelper helper;
    private WebView webView;
    private int currentlyDisplayedImage;
    private ArrayList<String> searchResults;

    public SearchFragment() {
        // apparently this empty public constructor is required.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout searchLayout = (RelativeLayout) inflater.inflate(R.layout.search_fragment,
                container, false); // gets the layout identified by R.layout.search_fragment, sets that as the view for this. A way to "summon" the xml file layout thing, I believe.
        Button searchButton = (Button) searchLayout.findViewById(R.id.search_button);
        Button backButton = (Button) searchLayout.findViewById(R.id.back_button);
        Button saveButton = (Button) searchLayout.findViewById(R.id.save_button);
        Button nextButton = (Button) searchLayout.findViewById(R.id.next_button);
        webView = (WebView) searchLayout.findViewById(R.id.results_webView);
        helper = new FeedDbHelper(getContext());
        currentlyDisplayedImage = -1;
        webView.loadUrl("http://wisdomchasers.net/wp-content/uploads/2014/11/helloworld.gif");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchField = (EditText) getActivity().findViewById(R.id.search_box);
                String thingToSearch = searchField.getText().toString();
                imageSearch(thingToSearch);
                Log.i("PRINTER", "testing");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrevious();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrent();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });

        handler = new HttpHandler(getActivity());
        return searchLayout;
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

    public void updateWebView() {
        String urlToShow = searchResults.get(currentlyDisplayedImage);
//        Toast.makeText(getActivity(), urlToShow, Toast.LENGTH_LONG).show();
        try {
            webView.loadUrl(urlToShow);
            Log.i("PRINTER", urlToShow);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("PRINTER", "WebView failed..." + "\t" + urlToShow);
        }
    }

    public void saveCurrent() {
        if (currentlyDisplayedImage != -1) {
            String currentUrl = searchResults.get(currentlyDisplayedImage);
            helper.writeToDatabase(currentUrl);
            Toast.makeText(getActivity(), "Saved.", Toast.LENGTH_SHORT).show();
        }
    }

    public void imageSearch(String searchQuery) {
        Toast.makeText(getActivity(), "Searching for \"" + searchQuery + "\"...", Toast.LENGTH_SHORT).show(); // Will probably erase this for the final version; right now it's useful having this feedback while debugging.
        handler.imageSearch(searchQuery, new Callback() {
            @Override
            public void callback(ArrayList<String> links, boolean success) {
                if (!success || links.get(0) == "ERROR") {
                    Log.e("Error", "Valid image locations not received.");
                } else {
                    currentlyDisplayedImage = 0;
                    searchResults = links;
                    updateWebView();
                }
            }

        });

    }
}
