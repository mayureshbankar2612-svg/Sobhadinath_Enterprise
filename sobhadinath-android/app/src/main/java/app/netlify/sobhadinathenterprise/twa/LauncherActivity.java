package app.netlify.sobhadinathenterprise.twa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    private static final String TARGET_URL = "https://sobhadinathenterprise.netlify.app/";
    private WebView webView;
    private RelativeLayout splashLayout;
    private RelativeLayout errorLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#234B35"));
        window.setNavigationBarColor(Color.parseColor("#234B35"));

        setContentView(createMainLayout());

        setupBackNavigation();

        if (isNetworkAvailable()) {
            loadWebsite();
        } else {
            showErrorView();
        }
    }

    private RelativeLayout createMainLayout() {
        RelativeLayout mainLayout = new RelativeLayout(this);
        mainLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setBackgroundColor(Color.parseColor("#F8F5E9"));

        // 1. Fullscreen WebView
        webView = new WebView(this);
        webView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        configureWebViewSettings();

        mainLayout.addView(webView);

        // 2. Offline Error Layout
        errorLayout = new RelativeLayout(this);
        errorLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        errorLayout.setBackgroundColor(Color.parseColor("#F8F5E9"));
        errorLayout.setVisibility(View.GONE);

        RelativeLayout errorContent = new RelativeLayout(this);
        RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView errorTitle = new TextView(this);
        errorTitle.setId(View.generateViewId());
        errorTitle.setText("No Internet Connection");
        errorTitle.setTextSize(20);
        errorTitle.setTextColor(Color.parseColor("#234B35"));
        errorTitle.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView errorMsg = new TextView(this);
        errorMsg.setId(View.generateViewId());
        RelativeLayout.LayoutParams msgParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        msgParams.addRule(RelativeLayout.BELOW, errorTitle.getId());
        msgParams.topMargin = 16;
        errorMsg.setLayoutParams(msgParams);
        errorMsg.setText("Please check your network settings and try again.");
        errorMsg.setTextSize(14);
        errorMsg.setTextColor(Color.parseColor("#555555"));

        Button retryButton = new Button(this);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.BELOW, errorMsg.getId());
        btnParams.topMargin = 32;
        retryButton.setLayoutParams(btnParams);
        retryButton.setText("Retry");
        retryButton.setTextColor(Color.WHITE);
        retryButton.setBackgroundColor(Color.parseColor("#234B35"));
        retryButton.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                errorLayout.setVisibility(View.GONE);
                splashLayout.setVisibility(View.VISIBLE);
                loadWebsite();
            } else {
                Toast.makeText(this, "Still offline. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

        errorContent.addView(errorTitle);
        errorContent.addView(errorMsg);
        errorContent.addView(retryButton);
        errorLayout.addView(errorContent, centerParams);
        mainLayout.addView(errorLayout);

        // 3. Splash Screen Overlay
        splashLayout = new RelativeLayout(this);
        splashLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        splashLayout.setBackgroundColor(Color.parseColor("#F8F5E9"));

        ImageView splashImage = new ImageView(this);
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(300, 300);
        imgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        splashImage.setLayoutParams(imgParams);

        int splashResId = getResources().getIdentifier("splash", "drawable", getPackageName());
        if (splashResId != 0) {
            splashImage.setImageResource(splashResId);
        } else {
            splashImage.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        splashLayout.addView(splashImage);
        mainLayout.addView(splashLayout);

        return mainLayout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebViewSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                return handleUrl(uri);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (splashLayout != null && splashLayout.getVisibility() == View.VISIBLE) {
                    splashLayout.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .withEndAction(() -> splashLayout.setVisibility(View.GONE))
                            .start();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    showErrorView();
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
    }

    private boolean handleUrl(Uri uri) {
        if (uri == null) return false;

        String url = uri.toString();
        String scheme = uri.getScheme();

        // 1. WhatsApp Intent Handling
        if (url.contains("wa.me") || url.contains("api.whatsapp.com") || "whatsapp".equalsIgnoreCase(scheme)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        // 2. Phone Call Handling
        if ("tel".equalsIgnoreCase(scheme)) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        // 3. Email Handling
        if ("mailto".equalsIgnoreCase(scheme)) {
            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        // 4. PDF Files Handling
        if (url.endsWith(".pdf")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        // 5. External domains (non-Sobhadinath)
        if (scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            if (uri.getHost() != null && uri.getHost().contains("sobhadinathenterprise.netlify.app")) {
                return false; // Load inside WebView
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }

        return false;
    }

    private void loadWebsite() {
        webView.loadUrl(TARGET_URL);
    }

    private void showErrorView() {
        if (splashLayout != null) splashLayout.setVisibility(View.GONE);
        if (errorLayout != null) errorLayout.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }

    private void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }
}
