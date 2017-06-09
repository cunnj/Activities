package com.cunnj.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.thirdparty.LauncherIconCreator;

public class ShortcutEditDialogFragment extends DialogFragment {
	protected MyActivityInfo activity;
	protected EditText text_name;
	protected ImageButton image_icon;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ComponentName activity = getArguments().getParcelable("activity");
		this.activity = new MyActivityInfo(activity, getActivity().getPackageManager());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_edit_activity, null);
		
		this.text_name = (EditText)view.findViewById(R.id.editText_name);
		this.text_name.setText(this.activity.name);
		
		this.image_icon = (ImageButton)view.findViewById(R.id.iconButton);
		ShortcutEditDialogFragment.this.image_icon.setImageDrawable(this.activity.icon);
		
		builder.setTitle(R.string.context_action_edit)
				.setView(view)
				.setPositiveButton(R.string.context_action_shortcut, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ShortcutEditDialogFragment.this.activity.name = ShortcutEditDialogFragment.this.text_name.getText().toString();
						PackageManager pm = getActivity().getPackageManager();
						try {
							final String icon_resource_string = ShortcutEditDialogFragment.this.activity.icon_resource_name; 
							final String pack = icon_resource_string.substring(0, icon_resource_string.indexOf(':'));
							final String type = icon_resource_string.substring(icon_resource_string.indexOf(':') + 1, icon_resource_string.indexOf('/'));
							final String name = icon_resource_string.substring(icon_resource_string.indexOf('/') + 1, icon_resource_string.length());
							
							Resources resources = pm.getResourcesForApplication(pack);
							ShortcutEditDialogFragment.this.activity.icon_resource = resources.getIdentifier(name, type, pack);
							if(ShortcutEditDialogFragment.this.activity.icon_resource != 0) {
								ShortcutEditDialogFragment.this.activity.icon = (BitmapDrawable)resources.getDrawable(ShortcutEditDialogFragment.this.activity.icon_resource);
							} else {
								ShortcutEditDialogFragment.this.activity.icon = (BitmapDrawable)pm.getDefaultActivityIcon();
							}
						} catch (NameNotFoundException e) {
							ShortcutEditDialogFragment.this.activity.icon = (BitmapDrawable)pm.getDefaultActivityIcon();
						} catch (Exception e) {
							ShortcutEditDialogFragment.this.activity.icon = (BitmapDrawable)pm.getDefaultActivityIcon();
						}
						
						LauncherIconCreator.createLauncherIcon(getActivity(), ShortcutEditDialogFragment.this.activity);
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 ShortcutEditDialogFragment.this.getDialog().cancel();
					}
				});

		return builder.create();
	}
}
