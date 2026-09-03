package com.github.peonyking.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Process;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.github.peonyking.BuildConfig;
import com.github.peonyking.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Global crash debugger.
 *
 * <p>Catches every uncaught exception in the app. When crash log is enabled in
 * settings, the full stack trace is written to a file under the app's file
 * directory. The newest unread report is then shown as a dialog on the next
 * launch (from {@link #checkAndShowPendingReport(Activity)}), and can always be
 * reviewed again from Settings → "View crash log". The dialog body is a normal
 * selectable {@link TextView}, so the text can be long pressed and copied.</p>
 *
 * <p>Created for debug purposes.</p>
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String REPORT_DIR = "crash_reports";
    private static final int MAX_REPORT_COUNT = 20;
    private static final String REPORT_PREFIX = "crash_";
    private static final String REPORT_SUFFIX = ".txt";

    private static CrashHandler sInstance;

    private final Context context;
    @Nullable
    private final Thread.UncaughtExceptionHandler defaultHandler;

    private CrashHandler(@NonNull Context context) {
        this.context = context.getApplicationContext();
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * Install the global handler once. Must be called from
     * {@link android.app.Application#onCreate()}.
     */
    public static void init(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new CrashHandler(context);
            Thread.setDefaultUncaughtExceptionHandler(sInstance);
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        boolean saved = false;
        try {
            if (PrefUtils.isCrashReportEnable()) {
                saveReport(thread, throwable);
                saved = true;
            }
        } catch (Throwable ignored) {
            // never crash inside the crash handler
        }
        if (saved) {
            // let the report be noticed at next launch
            PrefUtils.clearKey(PrefUtils.CRASH_REPORT_LAST_READ);
        }
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(thread, throwable);
        } else {
            Process.killProcess(Process.myPid());
            System.exit(10);
        }
    }

    private void saveReport(@NonNull Thread thread, @NonNull Throwable throwable) {
        File dir = getReportDir();
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }
        String time = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS", Locale.getDefault())
                .format(new Date());
        File report = new File(dir, REPORT_PREFIX + time + REPORT_SUFFIX);
        FileWriter writer = null;
        try {
            writer = new FileWriter(report);
            writer.write(buildReportText(thread, throwable));
        } catch (IOException ignored) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {
                }
            }
        }
        trimReports();
    }

    @NonNull
    private String buildReportText(@NonNull Thread thread, @NonNull Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("OpenHub crash report");
        sb.append("\n==========================================\n");
        sb.append("Time       : ")
                .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
                        .format(new Date()))
                .append('\n');
        sb.append("App        : ")
                .append(BuildConfig.APPLICATION_ID).append(" v")
                .append(BuildConfig.VERSION_NAME).append(" (")
                .append(BuildConfig.VERSION_CODE).append(") ")
                .append(BuildConfig.BUILD_TYPE)
                .append('\n');
        sb.append("Android    : ").append(Build.VERSION.RELEASE)
                .append(" (API ").append(Build.VERSION.SDK_INT).append(")\n");
        sb.append("Device     : ").append(Build.MANUFACTURER).append(' ')
                .append(Build.MODEL).append(" (").append(Build.DEVICE).append(")\n");
        sb.append("Process    : ").append(Process.myPid()).append('\n');
        sb.append("Thread     : ").append(thread.getName()).append('\n');
        sb.append("==========================================\n");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        sb.append(sw.toString());
        return sb.toString();
    }

    private void trimReports() {
        List<File> files = getReportFiles();
        if (files.size() <= MAX_REPORT_COUNT) {
            return;
        }
        // files are sorted newest first, so remove the tail
        for (int i = MAX_REPORT_COUNT; i < files.size(); i++) {
            //noinspection ResultOfMethodCallIgnored
            files.get(i).delete();
        }
    }

    @NonNull
    private File getReportDir() {
        return new File(context.getFilesDir(), REPORT_DIR);
    }

    /**
     * Newest first.
     */
    @NonNull
    private List<File> getReportFiles() {
        File dir = getReportDir();
        File[] files = dir.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        List<File> list = new ArrayList<>(Arrays.asList(files));
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
        return list;
    }

    @Nullable
    public static File getLatestReport() {
        if (sInstance == null) {
            return null;
        }
        List<File> files = sInstance.getReportFiles();
        return files.isEmpty() ? null : files.get(0);
    }

    /**
     * Whether there is at least one report that has not been shown to the user
     * yet in this or a previous session.
     */
    public static boolean hasUnreadReport() {
        File latest = getLatestReport();
        if (latest == null) {
            return false;
        }
        String lastRead = PrefUtils.getLastReadCrashReport();
        return !latest.getName().equals(lastRead);
    }

    /**
     * Remember that the current newest report has been shown, so it is not
     * popped up again on every launch.
     */
    public static void markReportsRead() {
        File latest = getLatestReport();
        if (latest != null) {
            PrefUtils.set(PrefUtils.CRASH_REPORT_LAST_READ, latest.getName());
        }
    }

    /**
     * Clear all saved crash reports.
     */
    public static void clearReports() {
        if (sInstance == null) {
            return;
        }
        for (File file : sInstance.getReportFiles()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    @NonNull
    public static String readReport(@NonNull File report) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(report));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return sb.toString();
    }

    /**
     * Show the newest unread crash report once per process, if any. Call it
     * from the first screen that becomes visible after a crash (e.g. main or
     * login activity), so the debugger pops up right after the app is opened.
     */
    public static void checkAndShowPendingReport(@NonNull final Activity activity) {
        if (!PrefUtils.isCrashReportEnable() || !hasUnreadReport()) {
            return;
        }
        markReportsRead();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLatestReportDialog(activity);
            }
        });
    }

    /**
     * Show the newest crash report in a dialog. The body is a selectable text
     * view, so the content can be long pressed and copied.
     */
    public static void showLatestReportDialog(@NonNull final Activity activity) {
        final File latest = getLatestReport();
        if (latest == null) {
            Toasty.info(activity, activity.getString(R.string.crash_report_empty)).show();
            return;
        }
        final String content = readReport(latest);
        ScrollView scrollView = new ScrollView(activity);
        scrollView.setFillViewport(false);
        TextView textView = new TextView(activity);
        textView.setTextSize(11f);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setTextIsSelectable(true);
        textView.setPadding(dp(activity, 16), dp(activity, 16),
                dp(activity, 16), dp(activity, 16));
        textView.setText(content);
        ScrollView.LayoutParams scrollLp = new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollView.addView(textView, scrollLp);
        int maxHeight = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.55f);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, maxHeight);
        scrollView.setLayoutParams(lp);

        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.crash_report_dialog_title)
                        + " · " + latest.getName())
                .setView(scrollView)
                .setPositiveButton(R.string.crash_copy_all, (dialog, which) -> {
                    AppUtils.copyToClipboard(activity, content);
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.close, null)
                .setNeutralButton(R.string.crash_clear, (dialog, which) -> {
                    clearReports();
                    Toasty.info(activity, activity.getString(R.string.crash_report_cleared)).show();
                    dialog.dismiss();
                })
                .show();
    }

    private static int dp(@NonNull Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * Share the newest report through the system share sheet.
     */
    public static void shareLatestReport(@NonNull Activity activity) {
        File latest = getLatestReport();
        if (latest == null) {
            Toasty.info(activity, activity.getString(R.string.crash_report_empty)).show();
            return;
        }
        String content = readReport(latest);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.crash_report_dialog_title));
        intent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share)));
    }
}
