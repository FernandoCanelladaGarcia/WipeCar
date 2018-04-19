package tfg.android.fcg;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;


@SuppressWarnings("rawtypes")
public class AppMediador extends Application {

	private static AppMediador singleton;

	// presenters and views members
//	private IEMERAppPresenter emmerAppPresenter;
//	private IEMERAppView emmerAppView;
//	private IInsertPresenter insertPresenter;
//	private IInsertView insertView;
//	private IDetailPresenter detailPresenter;
//	private IDetailView detailView;
//	private IMapPresenter mapPresenter;

	public static AppMediador getInstance(){
		return singleton;
	}

	// constants of commuinication, storage and petition
	public static final String NOTIFICATION_OPERATIONS_RECOVERED = "android.cnog.NOTIFICATION_OPERATIONS_RECOVERED";
    public static final String NOTIFICATION_RECORDING_ENDED = "android.cnog.NOTIFICATION_RECORDING_ENDED";
	public static final String NOTIFICATION_PLAYING_ENDED = "android.cnog.NOTIFICATION_PLAYING_ENDED";
    public static final String NOTIFICATION_INFO_RECOVERED = "android.cnog.NOTIFICATION_INFO_RECOVERED";
	public static final String NOTIFICATION_INFO_ADDED = "android.cnog.NOTIFICATION_INFO_ADDED";
	public static final String NOTIFICATION_INFO_REMOVED = "android.cnog.NOTIFICATION_INFO_REMOVED";
	public static final String NOTIFICATION_INIT_APP = "android.cnog.NOTIFICATION_INIT_APP";
	public static final String NOTIFICATION_GPS_LOCATION = "android.cnog.NOTIFICATION_GPS_LOCATION";

	public static final String APP_STATUS = "APP_STATUS";
    public static final String SET_OF_OPERATIONS = "SET_OF_OPERATIONS";
	public static final String INFO_OPERATION = "INFO_OPERATION";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
	public static final String ADDRESS = "ADDRESS";
	public static final String SEX = "SEX";
	public static final String AGE = "AGE";
	public static final String LATITUDE = "LATITUDE";
	public static final String LONGITUDE = "LONGITUDE";
	public static final String INSERT_STATUS = "INSERT_STATUS";
	public static final String OK = "OK";
	public static final String CANCEL = "CANCEL";

    public static final int DATABASE_ERROR = 0;
    public static final int STORAGE_ERROR = 1;
	public static final int APP_RUNNING = 2;
	public static final int INSERT_CODE = 0;

    public static boolean permission_granted = true;

	// Accessor methods to presenters and views
//	public IEMERAppPresenter getEmmerAppPresenter() {
//		if (emmerAppPresenter == null)
//			emmerAppPresenter = new EMERAppPresenter();
//		return emmerAppPresenter;
//	}
//
//	public void removeEmmerAppPresenter() {
//		emmerAppPresenter = null;
//	}
//
//    public IInsertPresenter getInsertPresenter() {
//        if (insertPresenter == null)
//            insertPresenter = new InsertPresenter();
//        return insertPresenter;
//    }
//
//    public void removeInsertPresenter() {
//        insertPresenter = null;
//    }
//
//    public IDetailPresenter getDetailPresenter() {
//        if (detailPresenter == null)
//            detailPresenter = new DetailPresenter();
//        return detailPresenter;
//    }
//
//    public void removeDetailPresenter() {
//        detailPresenter = null;
//    }
//
//	public IMapPresenter getMapPresenter() {
//		if (mapPresenter == null)
//			mapPresenter = new MapPresenter();
//		return mapPresenter;
//	}
//
//	public void removeMapPresenter() {
//		mapPresenter = null;
//	}
//
//	public IEMERAppView getEmmerAppView() {
//		return emmerAppView;
//	}
//
//	public void setEmmerAppView(IEMERAppView emmerAppView) {
//
//		this.emmerAppView = emmerAppView;
//	}
//
//	public IInsertView getInsertView() {
//	    return insertView;
//    }
//
//    public void setInsertView(IInsertView insertView) {
//	    this.insertView = insertView;
//    }
//
//    public IDetailView getDetailView() {
//	    return detailView;
//    }
//
//    public void setDetailView(IDetailView detailView) {
//	    this.detailView = detailView;
//    }
//
//	// Navigation and service definition methods
//    public Class navegateToInsertView() {
//	    return InsertView.class;
//    }
//
//    public Class navegateToDetailView() { return DetailView.class; }

	// Methods to manage Android components

	public void launchActivity(Class toActivity, Object fromComponent, Bundle extras) {
		Intent i = new Intent(this, toActivity);
		if (extras != null)
			i.putExtras(extras);	
		if (!fromComponent.getClass().equals(Activity.class))
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	public void launchActivity(Intent intent) {
		startActivity(intent);
	}
	
	public void launchActivityForResult(Class toActivity,
			Activity fromActivity, int requestCode, Bundle extras) {
		Intent i = new Intent(fromActivity, toActivity);
		if (extras != null)
			i.putExtras(extras);
		fromActivity.startActivityForResult(i, requestCode);
	}
	
	public void launchService(Class toService, Bundle extras) {
		Intent i = new Intent(this, toService);
		if (extras != null)
			i.putExtras(extras);
        startService(i);
	}
	
	public void stopService(Class toService) {
		Intent i = new Intent(this, toService);
        stopService(i);
	}
	
	public void registerReceiver(BroadcastReceiver receiver, String action) {
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(action));
	}	
	
	public void unRegisterReceiver(BroadcastReceiver receiver) {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}
	
	public void sendBroadcast(String action, Bundle extras) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (extras != null)
			intent.putExtras(extras);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		emmerAppPresenter = null;
//        insertPresenter = null;
//        detailPresenter = null;
		singleton = this;
	}
}
