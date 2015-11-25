package com.peterwang.toys.basic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.peterwang.toys.R;

/**
 * 滚动控件 Scroller 滚动测试
 *
 * @author peter_wang
 * @create-time 15/11/9 08:52
 */
public class ScrollerTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller_test);

        testTask();
    }

    private void testTask() {
        for (int i = 0; i < 1000; i++) {
            TestMyAsyncTask testMyAsyncTask = new TestMyAsyncTask();
            testMyAsyncTask.execute(1,2,3);
        }
    }

    private static class TestMyAsyncTask extends MyAsyncTask<Integer, Void, Void> {

        private static int mIndex = 0;

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
                Log.i("test", "task:" + mIndex++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
