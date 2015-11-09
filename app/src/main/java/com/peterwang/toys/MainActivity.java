package com.peterwang.toys;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.peterwang.toys.basic.ScrollerTestActivity;
import com.peterwang.toys.main.FeatureView;
import com.peterwang.toys.mapdraw.MapDrawActivity;

/**
 * 展示页面
 *
 * @author peter_wang
 * @create-time 15/11/6 08:23
 */
public final class MainActivity extends ListActivity {
    private static class DemoDetails {
        private final int mTitleId;
        private final int mDescriptionId;
        private final Class<? extends android.app.Activity> activityClass;

        public DemoDetails(int titleId, int descriptionId,
                           Class<? extends android.app.Activity> activityClass) {
            super();
            this.mTitleId = titleId;
            this.mDescriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            featureView.setTitleId(demo.mTitleId);
            featureView.setDescriptionId(demo.mDescriptionId);
            return featureView;
        }
    }

    private static final DemoDetails[] DEMOS = {
            new DemoDetails(R.string.map_draw, R.string.map_draw_description,
                    MapDrawActivity.class),
            new DemoDetails(R.string.scroller_test, R.string.scroller_test_description,
                    ScrollerTestActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("View toys Demo");
        ListAdapter adapter = new CustomArrayAdapter(
                this.getApplicationContext(), DEMOS);
        setListAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this.getApplicationContext(),
                demo.activityClass));
    }
}
