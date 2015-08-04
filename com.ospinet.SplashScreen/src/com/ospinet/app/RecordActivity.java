package com.ospinet.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.ospinet.app.R;

public class RecordActivity extends Activity implements ISideNavigationCallback {
	EditText edtTitle, edtTags, edtDescription;
	static String picturePath, imageFilePath, pdfPath;
	byte[] encodeByte;
	byte[] encodeByte1;
	byte[] pdfBytes;
	int flag = 0;

	private final int TAKE_PICTURE = 0;
	private final int RESULT_LOAD_IMAGE = 1;
	private final int SELECT_PDF = 2;
	Button btnAdd, btnCancel;
	static ProgressDialog dialogP;
	Spinner spYear, spMonth, spDay;
	String birth_info = "";
	String gender = "";
	String arr2[];
	int res = 0;
	String memid="";
	ImageView imgFile;
	private SideNavigationView sideNavigationView;
	@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

	setContentView(R.layout.record_add);
	showActionBar();
	 sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
     sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
     sideNavigationView.setMenuClickCallback(this);
     sideNavigationView.setMode(Mode.LEFT);

	dialogP = new ProgressDialog(RecordActivity.this);
	edtTitle = (EditText) findViewById(R.id.edtTitle);
	edtTags = (EditText) findViewById(R.id.edtTags);
	edtDescription = (EditText) findViewById(R.id.edtDescription);
	btnAdd = (Button) findViewById(R.id.btnAdd);
	btnCancel = (Button) findViewById(R.id.btnCancel);
	memid = getIntent().getStringExtra("member_id");
	imgFile = (ImageView) findViewById(R.id.imgFile);
}

public void Add_Record(View v)
{
	if (edtTitle.getText().toString().equals("")) {
		edtTitle.requestFocus();
		Toast.makeText(RecordActivity.this, "Please enter title",
				Toast.LENGTH_LONG).show();
		return;
	}
	if (edtDescription.getText().toString().equals("")) {
		edtDescription.requestFocus();
		Toast.makeText(RecordActivity.this, "Please enter description",
				Toast.LENGTH_LONG).show();
		return;
	}
	new AddRecordAsync().execute();

}

public class AddRecordAsync extends AsyncTask<String, String, String> {

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialogP.setMessage("Please Wait..");
		dialogP.show();
		dialogP.setCancelable(false);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String retstring = "";
		try {

			String title = edtTitle.getText().toString();
			String tagname = edtTags.getText().toString();
			String description = edtDescription.getText().toString();
			
			ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
			loginParam.add(new BasicNameValuePair("member_id", memid));
			loginParam.add(new BasicNameValuePair("title", title));
			loginParam.add(new BasicNameValuePair("tagname", tagname));
			loginParam.add(new BasicNameValuePair("description", description));
			
			
			
			/*String response = CustomHttpClient.executeHttpPost(
					"http://ospinet.com/app_ws/android_app_fun/add_record",
					loginParam);
			*/
			String response="";
			if (flag == 1) {
				if(imageFilePath!=null && imageFilePath.length()>0)
				loginParam.add(new BasicNameValuePair("filename", imageFilePath));
				response = CustomHttpClient
						.executeHttpPostForImg(
								"http://ospinet.com/app_ws/android_app_fun/add_record",
								loginParam, "filename",encodeByte);
				
			}
			else if (flag == 2) {
				//String image =Base64.encodeToString(encodeByte1, Base64.DEFAULT).trim();
				if(picturePath!=null && picturePath.length()>0)
				loginParam.add(new BasicNameValuePair("filename", picturePath));
				response = CustomHttpClient
						.executeHttpPostForImg(
								"http://ospinet.com/app_ws/android_app_fun/add_record",
								loginParam, "filename", encodeByte1);
				
			}
			else if (flag == 3) {
				//String image =Base64.encodeToString(encodeByte1, Base64.DEFAULT).trim();
				if(pdfPath!=null && pdfPath.length()>0)
				loginParam.add(new BasicNameValuePair("filename", pdfPath));
				response = CustomHttpClient
						.executeHttpPostForImg(
								"http://ospinet.com/app_ws/android_app_fun/add_record",
								loginParam, "filename", pdfBytes);
				
			}
			else
			{
			 response = CustomHttpClient.executeHttpPost(
						"http://ospinet.com/app_ws/android_app_fun/add_record",
						loginParam);
			}
			retstring = response.toString();
		} catch (Exception io) {
			String a ="";
		}
		return retstring;

	}

	@Override
	protected void onPostExecute(String sJson) {
 		if (dialogP.isShowing())
			dialogP.dismiss();
		try {
			/*JSONObject jsonResponse = new JSONObject(sJson);
			String jsonMainNode = jsonResponse.getString("success");*/
			if (sJson.contains("\"success\":1")) {
				Toast.makeText(RecordActivity.this,
						"Record added successfully", Toast.LENGTH_LONG)
						.show();
				Intent i = new Intent(RecordActivity.this,
						Records_Home.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("member_id", memid);
				i.putExtra("EXIT", true);
				
				RecordActivity.this.startActivity(i);
			//	finish();
			} else {
				Toast.makeText(RecordActivity.this,
						"Some problem. Please try again.",
						Toast.LENGTH_LONG).show();

			}
		} catch (Exception ex) {

		}
	}
}

public void cancel(View v)
{
	Intent intent = new Intent(RecordActivity.this, Records_Home.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("member_id", memid);
	intent.putExtra("EXIT", true);
	RecordActivity.this.startActivity(intent);

}



public void upload_image(View v)
{
	try
	{
		final Dialog builder = new Dialog(RecordActivity.this);
		final View view = getLayoutInflater().inflate(
				R.layout.capgaldailog, null);
		builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		builder.setContentView(view);
		Button addgal = (Button) view.findViewById(R.id.takegal);
		addgal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				builder.dismiss();
				builder.cancel();

				Intent i = new Intent(
						Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		Button addcam = (Button) view.findViewById(R.id.capture);
		addcam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				builder.dismiss();
				builder.cancel();

				Intent intent = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				Uri fileUri = getOutputMediaFileUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(intent, TAKE_PICTURE);

			}
		});
		Button uploadPDF = (Button) view.findViewById(R.id.uploadPDF);
		uploadPDF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				builder.dismiss();
				builder.cancel();

				
				Intent intent = new Intent();
				intent.setType("application/pdf");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select PDF"), SELECT_PDF);

			}
		});
		builder.show();


	}
	catch(Exception ex)
	{
		
	}
}

private static Uri getOutputMediaFileUri() {
	return Uri.fromFile(getOutputMediaFile());
}
private static File getOutputMediaFile() {
	// To be safe, you should check that the SDCard is mounted
	// using Environment.getExternalStorageState() before doing this.

	File mediaStorageDir = new File(
			Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
			"ABBYY Cloud OCR SDK Demo App");
	if (!mediaStorageDir.exists()) {
		if (!mediaStorageDir.mkdirs()) {
			return null;
		}
	}
	File mediaFile = new File(mediaStorageDir.getPath() + File.separator
			+ "image.jpg");

	return mediaFile;
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);

	try {
		if (resultCode != Activity.RESULT_OK)
			return;

		//String imageFilePath = null;
		Matrix matrix = new Matrix();
		switch (requestCode) {
		case TAKE_PICTURE:
			flag = 1;
			imageFilePath = getOutputMediaFileUri().getPath();
			// bitmap = BitmapFactory.decodeFile(imageFilePath);
			Bitmap bitmap = decodeScaledBitmapFromSdCard(imageFilePath, 72,
					72);
			// Bitmap bp= BitmapFactory.decodeFile(picturePath);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, bos);
			byte[] bb = bos.toByteArray();
			// convert image to string
			String image = Base64.encodeToString(bb, Base64.DEFAULT).trim();
			encodeByte = Base64.decode(image, Base64.DEFAULT);
			
			ExifInterface ei = new ExifInterface(imageFilePath);
			int orientation = ei.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				Matrix matrix2 = new Matrix();

				matrix2.postRotate(90);

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 72,
						72, true);

				bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
						scaledBitmap.getWidth(), scaledBitmap.getHeight(),
						matrix2, true);

				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.postRotate(180);
				break;
			// etc.
				
			}
			imgFile.setImageBitmap(bitmap);
			break;
		case SELECT_PDF:
			flag = 3;
			Uri selectedPDF = data.getData();
			 String[] projection = null;
			    String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
			    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
			    String[] selectionArgsPdf = new String[]{mimeType};
			    String sortOrder = MediaStore.Files.FileColumns.MIME_TYPE; // unordered
			    final Cursor allNonMediaFiles = managedQuery(selectedPDF, projection, selectionMimeType, selectionArgsPdf, sortOrder);
			    allNonMediaFiles.moveToFirst();
			    
			    pdfPath = getPath(RecordActivity.this, selectedPDF);
			/*
			    int colIndex = allNonMediaFiles.getColumnIndex(projection[0]);
				pdfPath = allNonMediaFiles.getString(colIndex);
			*/
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			FileInputStream fis;  
			try {  
				
			fis = new FileInputStream(new File(getPath(RecordActivity.this, selectedPDF)));  
			byte[] buf = new byte[1024];  
			int n;  
			while (-1 != (n = fis.read(buf)))  
			baos.write(buf, 0, n);  
			} catch (Exception e) {  
			e.printStackTrace();  
			}  
			pdfBytes = baos.toByteArray();
			    
			Toast.makeText(RecordActivity.this, "PDF SELECTED", Toast.LENGTH_LONG).show();
			break;
		case RESULT_LOAD_IMAGE:
			flag = 2;
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bp = BitmapFactory.decodeFile(picturePath);
			ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
			bp.compress(CompressFormat.PNG, 100, bos1);
			byte[] bb1 = bos1.toByteArray();
			// convert image to string
			String image1 = Base64.encodeToString(bb1, Base64.DEFAULT)
					.trim();
			encodeByte1 = Base64.decode(image1, Base64.DEFAULT);
			;
			//uplaodedimg.setImageBitmap(bp);
			imgFile.setImageBitmap(bp);
		}

	} catch (OutOfMemoryError e) {

	} catch (Exception e) {

	}

}
public static String getPath(final Context context, final Uri uri) {

    // check here to KITKAT or new version
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/"
                        + split[1];
            }
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[] { split[1] };

            return getDataColumn(context, contentUri, selection,
                    selectionArgs);
        }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {

        // Return the remote address
        if (isGooglePhotosUri(uri))
            return uri.getLastPathSegment();

        return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
        return uri.getPath();
    }

    return null;
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 * 
 * @param context
 *            The context.
 * @param uri
 *            The Uri to query.
 * @param selection
 *            (Optional) Filter used in the query.
 * @param selectionArgs
 *            (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
public static String getDataColumn(Context context, Uri uri,
        String selection, String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = { column };

    try {
        cursor = context.getContentResolver().query(uri, projection,
                selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            final int index = cursor.getColumnIndexOrThrow(column);
            return cursor.getString(index);
        }
    } finally {
        if (cursor != null)
            cursor.close();
    }
    return null;
}

/**
 * @param uri
 *            The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri
            .getAuthority());
}

/**
 * @param uri
 *            The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri
            .getAuthority());
}

/**
 * @param uri
 *            The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri
            .getAuthority());
}

/**
 * @param uri
 *            The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri
            .getAuthority());
}


public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
		int reqWidth, int reqHeight) {

	// First decode with inJustDecodeBounds=true to check dimensions
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(filePath, options);

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth,
			reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeFile(filePath, options);
}

public static int calculateInSampleSize(BitmapFactory.Options options,
		int reqWidth, int reqHeight) {

	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {

		final int heightRatio = Math.round((float) height
				/ (float) reqHeight);
		final int widthRatio = Math.round((float) width / (float) reqWidth);

		inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	}
	return inSampleSize;
}

	private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflator.inflate(R.layout.menu2, null);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowHomeEnabled (false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setCustomView(v);
    ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
    SearchView search = (SearchView) v.findViewById(R.id.search);
    imgAdd.setVisibility(View.INVISIBLE);
    search.setVisibility(View.INVISIBLE);
    ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);	
    imgAdd.setOnClickListener(new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(RecordActivity.this, RecordActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			intent.putExtra("member_id", memid);
			RecordActivity.this.startActivity(intent);
            
		}
	});
    imgMenu.setOnClickListener(new OnClickListener() {
    	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 sideNavigationView.toggleMenu();
			 RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
             rel.bringChildToFront(sideNavigationView);
            
		}
	});

    ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
    TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

    imgLogo.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(RecordActivity.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
    txtLogoName.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(RecordActivity.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
	}
	@Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(RecordActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                RecordActivity.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(RecordActivity.this, Member_Home.class);
                RecordActivity.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(RecordActivity.this, help.class);
                RecordActivity.this.startActivity(help);

                break;

            case R.id.side_navigation_menu_item4:
                Intent home = new Intent(RecordActivity.this, PreMemberHome.class);
                RecordActivity.this.startActivity(home);

                break;

            case R.id.side_navigation_menu_item5:
                Intent share = new Intent(RecordActivity.this, ShareMainActivity.class);
                RecordActivity.this.startActivity(share);

                break;

            case R.id.side_navigation_menu_item6:
                Intent search = new Intent(RecordActivity.this, SearchMainActivity.class);
                RecordActivity.this.startActivity(search);

                break;

            default:
                return;
        }
        // finish();
    }

	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(RecordActivity.this, Records_Home.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("member_id", memid);
		intent.putExtra("EXIT", true);
		RecordActivity.this.startActivity(intent);

	}
}
