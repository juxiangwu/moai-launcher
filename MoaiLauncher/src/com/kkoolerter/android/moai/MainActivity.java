package com.kkoolerter.android.moai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kkoolerter.android.moai.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private static final String TAG = "MoaiLauncher";
	public static final String DEFAULT_WORK_DIRECTORY = Environment
			.getExternalStorageDirectory() + "/moai";
	private static final String LAUNCHER_FILE = "laucher.properties";
	private static final String NO_MEDIA_FILE = ".nomedia";
	private ListView mProjectListView;
	private List<ProjectInfo> mProjects = new ArrayList<ProjectInfo>();
	private ProjectAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mProjectListView = (ListView) findViewById(R.id.main_apps_lsv);
		// mHandler.sendEmptyMessage(DO_LOAD_PROJECTS);
		mAdapter = new ProjectAdapter(this, mProjects);
		mProjectListView.setAdapter(mAdapter);
		mProjectListView.setOnItemClickListener(new OnProjectClickListener());
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessage(DO_LOAD_PROJECTS);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mProjects.clear();
	}

	private static final int DO_LOAD_PROJECTS = 0x01;
	private static final int DO_REFRESH_PROJECTS = 0x02;
	private static final int DO_PROJECT_SELECT = 0x03;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DO_LOAD_PROJECTS:
				doLoadAllProjects();
				break;
			case DO_REFRESH_PROJECTS:
				doRefreshProjects();
				break;
			case DO_PROJECT_SELECT:
				doProjectSelect(msg.arg1);
				break;
			}
		}
	};

	private void doProjectSelect(int pos) {
		try {
			ProjectInfo project = mProjects.get(pos);
			String workDirector = project.getStartDir();
			Intent intent = new Intent(this, MoaiActivity.class);
			intent.putExtra(MoaiActivity.KEY_WORK_DIRECTORY, workDirector);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doRefreshProjects() {
		mAdapter.notifyDataSetChanged();
	}

	private void doLoadAllProjects() {
		Log.d(TAG, "doLoadAllProjects:project dir = " + DEFAULT_WORK_DIRECTORY);
		File projectDir = new File(DEFAULT_WORK_DIRECTORY);
		if (!projectDir.exists()) {
			projectDir.mkdirs();
			File file = new File(projectDir.getAbsoluteFile() + "/"
					+ NO_MEDIA_FILE);
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Log.d(TAG, "doLoadAllProjects:make project dir");
			return;
		}

		String[] projects = projectDir.list();
		for (String prodir : projects) {
			Log.d(TAG, "doLoadAllProjects:prodir = " + prodir);
			File pf = new File(projectDir.getAbsolutePath() + "/" + prodir);
			Log.d(TAG, "doLoadAllProjects:pf = " + pf.getAbsolutePath());

			ProjectInfo pi = new ProjectInfo();
			pi.setRootDir(pf.getAbsolutePath());

			if (pf.isDirectory()) {
				File launcherFile = new File(pf.getAbsolutePath() + "/"
						+ LAUNCHER_FILE);
				Log.d(TAG,
						"doLoadAllProjects:launcher file = "
								+ launcherFile.getAbsolutePath());
				if (!launcherFile.exists()) {
					Log.d(TAG,
							"doLoadAllProjects:launcher config file not found");
					continue;
				}

				Properties lp = new Properties();
				InputStream is = null;
				try {
					is = new FileInputStream(launcherFile.getAbsoluteFile());
					lp.load(is);
					String name = lp.getProperty(ProjectInfo.KEY_NAME);
					String author = lp.getProperty(ProjectInfo.KEY_AUTHOR);
					String version = lp.getProperty(ProjectInfo.KEY_VERSION);
					String icon = lp.getProperty(ProjectInfo.KEY_ICON);
					String dir = pf.getAbsolutePath() + "/"
							+ lp.getProperty(ProjectInfo.KEY_PRO_DIR);
					Log.d(TAG, "doLauncherAllProjects:start dir = " + dir);
					pi.setAuthor(author);
					pi.setIcon(icon);
					pi.setName(name);
					pi.setStartDir(dir);
					pi.setVersion(version);
					mProjects.add(pi);
					Log.d(TAG, "doLoadAllProjects:" + pi.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				Log.d(TAG, "doLoadAllProjects:is file");
			}
		}

		mHandler.sendEmptyMessage(DO_REFRESH_PROJECTS);
	}

	private class OnProjectClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Message msg = new Message();
			msg.what = DO_PROJECT_SELECT;
			msg.arg1 = position;
			mHandler.sendMessage(msg);
		}

	}
}
