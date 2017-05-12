package com.example.trungnguyen.demohtmlview;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    WebView mWebView;
    String url = "http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-" +
            "tiep-vien-hang-khong-dep-nhat-singapore-1.jpg";
    ImageView detailNews;
    String mainHtml = "";
    NestedScrollView mNestedScrollView;
    FloatingActionButton mFabMore;
    FloatingActionButton mFabLike;
    FloatingActionButton mFabShare;
    FloatingActionButton mFabCmt;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    Animation mAnimShow;
    Animation mAnimHide;
    boolean isMoreFabShow;
    Animation mShowMore;
    Animation mHideMore;
    AppBarLayout appbar;
    NestedScrollView scrollView;

    private static final int SWIPE_MIN_DISTANCE = 130;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SWIPE_MIN_DISTANCE_Y = 100;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.wvDemo);
        detailNews = (ImageView) findViewById(R.id.detail_news_background);
        mFabMore = (FloatingActionButton) findViewById(R.id.fab_more);
        mFabLike = (FloatingActionButton) findViewById(R.id.fab_like);
        mFabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        mFabCmt = (FloatingActionButton) findViewById(R.id.fab_cmt);
        mFabMore.setOnClickListener(this);
        mFabLike.setOnClickListener(this);
        mFabShare.setOnClickListener(this);
        mFabCmt.setOnClickListener(this);
        isMoreFabShow = false;
        appbar = (AppBarLayout) findViewById(R.id.app_bar);
        final Animation mAnimBottomIn = AnimationUtils.loadAnimation(this, R.anim.bottom_in);
        final Animation mAnimBottomOut = AnimationUtils.loadAnimation(this, R.anim.bottom_out);
        mAnimShow = AnimationUtils.loadAnimation(this, R.anim.show_fab);
        mAnimHide = AnimationUtils.loadAnimation(this, R.anim.hide_fab);
        mShowMore = AnimationUtils.loadAnimation(this, R.anim.show_more);
        mHideMore = AnimationUtils.loadAnimation(this, R.anim.hide_more);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.d("NESTED", "ScrollX : " + scrollX);
//                Log.d("NESTED", "ScrollY : " + scrollY);
//                Log.d("NESTED", "oldScrollX : " + oldScrollX);
//                Log.d("NESTED", "oldScrollY : " + oldScrollY);
                if (scrollY > oldScrollY && mFabMore.getVisibility() == View.VISIBLE) {
                    if (!isMoreFabShow) {
                        mFabMore.startAnimation(mAnimBottomOut);
                        mFabMore.setVisibility(View.INVISIBLE);
                    }
                } else if (scrollY < oldScrollY && mFabMore.getVisibility() == View.INVISIBLE) {
                    mFabMore.setVisibility(View.VISIBLE);
                    mFabMore.startAnimation(mAnimBottomIn);
                }
            }
        });


        int imgHeight = 500;

        detailNews.requestLayout();


        detailNews.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, imgHeight, getResources().getDisplayMetrics());

        detailNews.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        detailNews.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(this).load(url)
                .fitCenter()
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(detailNews);

        mWebView.getSettings().setLoadsImagesAutomatically(true);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

//        mWebView.setPictureListener(this);

        gestureDetector = new GestureDetector(new MyGestureDetector());
//        gestureListener = new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);
//            }
//        };

//        appbar.setOnTouchListener(gestureListener);
//        mNestedScrollView.setOnTouchListener(gestureListener);


        //// TODO: 5/11/2017 "<style> img{display: inline;height: auto;max-width: 100%;} + a {color:black;text-decoration:none}\n\n </style>" + content news to fix image size match device screen and remove underline link and turn text color link to black

        final String configContent = "<style> " +
                "img{display: inline;height: auto;max-width: 100%;}" + "a {color:black;text-decoration:none}\n\n" +
                "</style>";

        mainHtml =
                configContent + "<div id=\"vb-content-detailbox\"><b>Sở hữu chiều cao nổi trội cùng khuôn mặt đẹp " +
                        "\"không tì vết\", Rita Kao được mệnh danh là tiếp viên hàng không đẹp nhất Singapore.</b> <p><a " +
                        "href=\"http://vietbao.vn/Dep/Xem-5-dieu-nay-ai-con-dam-bao-beo-khong-mac-duoc-bikini/55875283/108/" +
                        "\">Xem 5 điều này, ai còn dám bảo béo không mặc được bikini?</a></p> <p><a href=\"http://vietbao.vn/" +
                        "The-gioi-giai-tri/Say-long-truoc-ve-dep-nu-than-cua-trum-bat-dong-san-Ai-Cap/55875281/235/\">" +
                        "Say lòng trước vẻ đẹp nữ thần của trùm bất động sản Ai Cập</a></p> <p><a href=\"http://vietbao.vn/Dep/Vo" +
                        "-chong-Hari-Tran-Thanh-xai-do-hieu-ngay-cang-len-tay/55875278/108/\">Vợ chồng Hari - Trấn Thành xài đồ hiệu" +
                        " ngày càng \"lên tay\"?</a></p> <p>Với số lượng người theo dõi khủng trên mạng xã hội, Rita Kao được bầu " +
                        "chọn là tiếp viên hàng không đẹp nhất Singapore. Mỹ nữ sinh năm 1991 sinh ra và lớn lên ở Đài Loan và&nbsp" +
                        ";đang làm việc tại hãng hàng không Tiger Air.</p> <p>Ngoài vẻ đẹp nổi bật, chân dài còn thu hút bởi phong" +
                        " cách mặc đồ phóng khoáng đầy quyến rũ. Tận dụng chiều cao 1m7 và số đo hình thể đáng ngưỡng mộ 86-60-90," +
                        " cô luôn&nbsp;lựa chọn những bộ đồ kiệm vải. Bên cạnh đó, Rita cũng đặc biệt yêu thích bikini và&nbsp;các" +
                        " trang phục như áo trễ vai, áo hai dây hay mốt áo dấu quần khi đi du lịch.</p><div class=\"adspos adspos-" +
                        "4\">\n" +
                        "                        <div id=\"div-gpt-ad-1399432304639-4\" " +
                        "style=\"width:300px; margin: 0 auto; \">\n" +
                        "                            <script type=\"text/javascript\">\n" +
                        "                                if(window.innerWidth<900){\n" +
                        "                                    googletag.cmd.push(function() { googletag.display('div-gpt-ad-1399432304639-4'); });\n" +
                        "                                    ads_content_pos4 = 1;\n" +
                        "                                }\n" +
                        "                            </script>\n" +
                        "                   </div> </div><p></p> <p>Hình ảnh trẻ trung pha lẫn sự dịu dàng đã giúp Rita có hơn " +
                        "100.000 người theo dõi trên Instagram và hơn 400.000 hâm mộ trên Facebook." +
                        " Nhiều fan hâm mộ cho rắng&nbsp;Rita Kao trẻ hơn tuổi 26 của mình rất nhiều.&nbsp;</p> " +
                        "<p><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-" +
                        "dep-nhat-singapore-1.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong dep nhat Singapore\" " +
                        "class=\"img-share-slide img-share-slide-0\" imgrel=\"0\"></p> <p>Nhan sắc cô nàng tiếp viên hot nhất " +
                        "Sigapore.&nbsp;</p> <p><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-" +
                        "tiep-vien-hang-khong-dep-nhat-singapore-2.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong" +
                        " dep nhat Singapore\" class=\"img-share-slide img-share-slide-1\" imgrel=\"1\"></p> <p>Khi khoác lên bộ " +
                        "đồng phục Rita Kao càng làm nổi bật vẻ đẹp dịu dàng và nữ tính của mình.&nbsp;</p> <p><img " +
                        "src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien" +
                        "-hang-khong-dep-nhat-singapore-3.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien " +
                        "hang khong dep nhat Singapore\" class=\"img-share-slide img-share-slide-2\" " +
                        "imgrel=\"2\"></p> <p>Người đẹp thường xuyên diện các trang phục bikini kiệm vải.</p> <p><img" +
                        " src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat" +
                        "-singapore-4.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong dep nhat Singapore\" class=\"" +
                        "img-share-slide img-share-slide-3\" imgrel=\"3\"></p> <p>Ngoài chiều cao lý tưởng cùng số đo cân đối, " +
                        "mỹ nữ sở hữu khuôn mặt xinh đẹp và nụ cười tươi tắn.&nbsp;</p> <p><img src=\"http://a9.vietbao.vn/images/" +
                        "vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat-singapore-5.jpg\" alt=\"&quot;Me " +
                        "dam&quot; nhan sac tiep vien hang khong dep nhat Singapore\" class=\"img-share-slide img-share-slide-4\"" +
                        " imgrel=\"4\"></p> <p><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-t" +
                        "iep-vien-hang-khong-dep-nhat-singapore-6.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong de" +
                        "p nhat Singapore\" class=\"img-share-slide img-share-slide-5\" imgrel=\"5\"></p> <p>Nếu không tỏa sáng v" +
                        "ới bikini, Rita sẽ chọn những chiếc áo quây&nbsp;khoe vai trần quyến rũ.&nbsp;</p> <p><img src=\"http://" +
                        "a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat-singapore-7" +
                        ".jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong dep nhat Singapore\" class=\"img-share-sli" +
                        "de img-share-slide-6\" imgrel=\"6\"></p> <p>Áo hai dây trễ&nbsp;ngực cũng được cô nàng trưng dụng thường" +
                        " xuyên.&nbsp;</p> <p><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-ti" +
                        "ep-vien-hang-khong-dep-nhat-singapore-8.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong dep" +
                        " nhat Singapore\" class=\"img-share-slide img-share-slide-7\" imgrel=\"7\"></p> <p>Khi đi du lịch nước n" +
                        "goài, Rita khoe đôi chân dài bằng mốt áo dấu quần, người đẹp kết hợp với boots cao.&nbsp;</p> <p><img sr" +
                        "c=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat-" +
                        "singapore-9.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong dep nhat Singapore\" class=\"im" +
                        "g-share-slide img-share-slide-8\" imgrel=\"8\"></p> <p><img src=\"http://a9.vietbao.vn/images/vn999/55/2" +
                        "017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat-singapore-10.jpg\" alt=\"&quot;Me dam&quot" +
                        "; nhan sac tiep vien hang khong dep nhat Singapore\" class=\"img-share-slide img-share-slide-9\" imgrel=" +
                        "\"9\"></p> <p>Nữ tiếp viên cũng không thể bỏ qua những chiếc váy siêu&nbsp;ngắn này.&nbsp;</p> <p><img " +
                        "src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat" +
                        "-singapore-11.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong dep nhat Singapore\" " +
                        "class=\"img-share-slide img-share-slide-10\" imgrel=\"10\"></p> <p>Mốt áo ren xuyên thấu cũng thường" +
                        " xuyên được người đẹp lựa chọn.&nbsp;</p> <p><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/" +
                        "05/20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat-singapore-12.jpg\" alt=\"&quot;Me dam&quot;" +
                        " nhan sac tiep vien hang khong dep nhat Singapore\" class=\"img-share-slide img-share-slide-11\" " +
                        "imgrel=\"11\"></p> <p><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/20170511-me-dam-nhan-sac" +
                        "-tiep-vien-hang-khong-dep-nhat-singapore-13.jpg\" alt=\"&quot;Me dam&quot; nhan sac tiep vien hang khong " +
                        "dep nhat Singapore\" class=\"img-share-slide img-share-slide-12\" imgrel=\"12\"></p> <a " +
                        "href=\"http://vietbao.vn/Dep/Chan-dai-1m90-cong-khai-qua-trinh-vit-hoa-thien-nga-day-ngoan-muc/55875150" +
                        "/108/\" title=\"Chân dài 1m90 công khai quá trình \"><img src=\"http://a9.vietbao.vn/images/vn999/55/2017/05/" +
                        "20170511-me-dam-nhan-sac-tiep-vien-hang-khong-dep-nhat-singapore-14.jpg\" alt=\"&quot;Me dam&quot; nhan sac" +
                        " tiep vien hang khong dep nhat Singapore\" class=\"img-share-slide img-share-slide-13\" imgrel=\"13\"></a> " +
                        "<a href=\"http://vietbao.vn/Dep/Chan-dai-1m90-cong-khai-qua-trinh-vit-hoa-thien-nga-day-ngoan-muc/55875150" +
                        "/108/\">Chân dài 1m90 công khai quá trình \"vịt hóa thiên nga\" đầy ngoạn mục</a> <p>Người mẫu Hồng Xuân đã " +
                        "có sự thay đổi khá lớn về nhan sắc so với thời điểm tham gia cuộc thi Vietnam\"s Next Top Model...</p>" +
                        " <a href=\"http://vietbao.vn/Dep/Chan-dai-1m90-cong-khai-qua-trinh-vit-hoa-thien-nga-day-ngoan-muc/558" +
                        "75150/108/\" title=\"\">Bấm xem ngay &gt;&gt;</a> " + "</div>";


        mWebView.setWebViewClient(new MyCustomWebViewClient(mainHtml));

        mWebView.loadDataWithBaseURL("", mainHtml, mimeType, encoding, "");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_more:
                if (mFabCmt.getVisibility() == View.VISIBLE &&
                        mFabLike.getVisibility() == View.VISIBLE && mFabShare.getVisibility() == View.VISIBLE) {
                    isMoreFabShow = false;
                    hideOptionFab();

                } else {
                    isMoreFabShow = true;
                    showOptionFab();

                }
        }
    }

    private void showOptionFab() {
        mFabMore.startAnimation(mShowMore);

        mFabCmt.setVisibility(View.VISIBLE);

        mFabCmt.startAnimation(mAnimShow);

        mFabLike.setVisibility(View.VISIBLE);

        mFabLike.startAnimation(mAnimShow);

        mFabShare.setVisibility(View.VISIBLE);
        mFabShare.startAnimation(mAnimShow);

        if (!mFabCmt.isClickable() && !mFabLike.isClickable() && !mFabShare.isClickable()) {
            mFabCmt.setClickable(true);
            mFabLike.setClickable(true);
            mFabShare.setClickable(true);
        }
    }



    private void hideOptionFab() {
        mFabMore.startAnimation(mHideMore);

        mFabCmt.setVisibility(View.INVISIBLE);
        mFabCmt.startAnimation(mAnimHide);

        mFabLike.setVisibility(View.INVISIBLE);
        mFabLike.startAnimation(mAnimHide);

        mFabShare.setVisibility(View.INVISIBLE);
        mFabShare.startAnimation(mAnimHide);

        if (mFabCmt.isClickable() && mFabLike.isClickable() && mFabShare.isClickable()) {
            mFabCmt.setClickable(false);
            mFabLike.setClickable(false);
            mFabShare.setClickable(false);
        }
    }


    private class MyCustomWebViewClient extends WebViewClient {

        private String mHtml;

        public MyCustomWebViewClient(String currentHtml) {
            mHtml = currentHtml;
        }


        public void setHtml(String html) {
            mHtml = html;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(mHtml)) {
                mWebView.loadDataWithBaseURL("", mHtml, mimeType, encoding, "");
            }
            return true;
        }
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && (e1.getY() - e2.getY()) < SWIPE_MIN_DISTANCE_Y
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && (e1.getY() - e2.getY()) < SWIPE_MIN_DISTANCE_Y
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TouchEvent dispatcher.
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                // If the gestureDetector handles the event, a swipe has been
                // executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
