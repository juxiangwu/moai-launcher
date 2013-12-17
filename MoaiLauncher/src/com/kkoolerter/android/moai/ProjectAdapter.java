package com.kkoolerter.android.moai;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectAdapter extends BaseAdapter {
	private List<ProjectInfo> mProjects;
	private LayoutInflater mInflater;
	private String mProjectFormat;
	private String mAuthorFormat;
	private String mVersionFormat;

	public ProjectAdapter(Context context, List<ProjectInfo> infos) {
		mProjects = infos;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mProjectFormat = context.getString(R.string.label_project_name);
		mAuthorFormat = context.getString(R.string.label_project_author);
		mVersionFormat = context.getString(R.string.label_project_version);

	}

	@Override
	public int getCount() {

		return mProjects.size();
	}

	@Override
	public Object getItem(int position) {

		return mProjects.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.project_info_layout, null);
			holder.mIconImageView = (ImageView) convertView
					.findViewById(R.id.project_icon_imv);
			holder.mNameTextView = (TextView) convertView
					.findViewById(R.id.project_name_tv);
			holder.mAuthorTextView = (TextView) convertView
					.findViewById(R.id.project_author_tv);
			holder.mVersionTextView = (TextView) convertView
					.findViewById(R.id.project_version_tv);
			convertView.setTag(holder);
		}

		final ProjectInfo project = mProjects.get(position);

		File iconFile = new File(project.getRootDir() + "/" + project.getIcon());
		if (iconFile.exists()) {
			Bitmap icon = BitmapFactory.decodeFile(iconFile.getAbsolutePath());
			holder.mIconImageView.setImageBitmap(icon);
		} else {
			holder.mIconImageView.setImageDrawable(null);
		}

		holder.mAuthorTextView.setText(String.format(mAuthorFormat,
				project.getAuthor()));
		holder.mNameTextView.setText(String.format(mProjectFormat,
				project.getName()));
		holder.mVersionTextView.setText(String.format(mVersionFormat,
				project.getVersion()));

		return convertView;
	}

	private static final class ViewHolder {
		public ImageView mIconImageView;
		public TextView mNameTextView;
		public TextView mAuthorTextView;
		public TextView mVersionTextView;
	}

}
