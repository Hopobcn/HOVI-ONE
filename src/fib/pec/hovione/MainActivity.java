package fib.pec.hovione;

import java.util.ArrayList;
import java.util.Set;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements SensorEventListener {
	
	//////// Constants De La Classe ////////////////////////////////////////////////////////////////////////////////////////////	
	private static final int REQUEST_ENABLE_BT = 1; //constant per identificar peticio de engegar bluetooth
	
	/////// Atributs De La Classe /////////////////////////////////////////////////////////////////////////////////	
	//	private boolean bluetoothEnabled;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	
	private ViewPager mViewPager;//El ViewPager contindr� el contingut de les seccions. ViewPager es un tipus de layout que permet
                         // desplasarnos entre pantalles(tambe anomenades pagines o seccions) lliscant el dit horitzontalment. Al viewpager
	                     // se li ha de configurar un pagerAdapter que s'encarrega de crear les pagines que es mostrar�n.
	                     // Normalment les pagines del viewpager son fragments i es fa servir un adapter que tracta
	                     // les pagines com a fragments anomenat FragmentPageAdapter.
	
	private SectionsPagerAdapter mSectionsPagerAdapter; //FragmentPageAdapter que gestiona la creacio de fragments(pagines) per al viewpager.
	
	private ArrayList<String> foundDevicesList;
	
	private int mStackLevel = 0;
	
	private ActionBar actionBar;
	
	private BluetoothManager bluetoothManager;
	
	private BluetoothDevice mDeviceCon;
	
	private BTClientThread Conthread = null;
	
	private BTManagingConThread Manthread = null;
	
	private boolean primeraVegada;
	
	final Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == 0) {
				AlertDialogFragment dialogSuccConnected = AlertDialogFragment.newInstance(R.string.title_successful_con, getString(R.string.alert_text_successful_con));
				dialogSuccConnected.show(getSupportFragmentManager(), "BtConSucc");	
			} else if (msg.what == 1) {
				Bitmap mym = (Bitmap) msg.obj;	
				mSectionsPagerAdapter.destroyAndCreatePhoto(mym);
				mSectionsPagerAdapter.notifyDataSetChanged();				
			} else if (msg.what == 2) {
				AlertDialogFragment dialogSuccConnected = AlertDialogFragment.newInstance(R.string.title_unsuccessful_con, getString(R.string.alert_text_unsuccessful_con));
				dialogSuccConnected.show(getSupportFragmentManager(), "BtConUnSucc");	
			}
			
			mHandler.removeMessages(0);
			return false;
		}
	});
	
	private Sensor msensor;
	
	/////// Metodes /////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		primeraVegada = true;
		
		if (savedInstanceState != null) {
			mStackLevel = savedInstanceState.getInt("mStackLevel");
			primeraVegada = savedInstanceState.getBoolean("primeraVegada");
		}
		
		//inicialitzacions de variables
		actionBar = getActionBar();
		mViewPager = (ViewPager) findViewById(R.id.pager); //obtenim el viewpager del arxiu de layout xml de aquesta activity
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
					
		configureActionBar();
		configureViewPager();
		
		mDeviceCon = (BluetoothDevice) getLastCustomNonConfigurationInstance();
		
		bluetoothManager = new BluetoothManager(this);
		foundDevicesList = new ArrayList<String>();
		
		if (primeraVegada) {
			if (bluetoothManager.isBluetoothSupported()) {
				if (bluetoothManager.isBluetoothEnabled()) {
					Set<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevices();
					if (pairedDevices.size() > 0) {
				        for (BluetoothDevice device : pairedDevices) {
				            // Add the name and address to an array adapter to show in a ListView
				            foundDevicesList.add(device.getName() + "\n" + device.getAddress());
				        }
				    }
					showBTDialogFragment();
				}
				else {				
					bluetoothManager.enableBluetooth();                
				}
			}
			else {
				AlertDialogFragment dialogNoBtSupport = AlertDialogFragment.newInstance(R.string.title_warning, "Bluetooth No Soportat");
				dialogNoBtSupport.show(getSupportFragmentManager(), "BtNoSupported");			
			}
		} else {
			crearThreadConnexio(mDeviceCon);
		}
		
		SensorManager sman = (SensorManager) getSystemService(Context.SENSOR_SERVICE); 
		msensor = sman.getDefaultSensor(Sensor.TYPE_GRAVITY);
		if (msensor != null) {
			sman.registerListener(this, msensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		else {
			Toast.makeText(getApplicationContext(), "Sensor not availabe", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	  	savedInstanceState.putInt("mStackLevel", mStackLevel);
	  	savedInstanceState.putBoolean("primeraVegada", primeraVegada);
	}
	
	@Override
	public Object onRetainCustomNonConfigurationInstance () {
		return (Object) mDeviceCon;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (Manthread != null) Manthread.cancel();
		System.out.println("Manthread cancelat");
		if (Conthread != null) Conthread.cancel();
		System.out.println("Conthread cancelat");
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mReceiver);
        if (bluetoothManager.isBluetoothSupported()) bluetoothManager.endDiscovery(); 
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == REQUEST_ENABLE_BT) {
	    	if (resultCode == RESULT_OK) {
				Set<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevices();
				if (pairedDevices.size() > 0) {
			        for (BluetoothDevice device : pairedDevices) {
			            // Add the name and address to an array adapter to show in a ListView
			            foundDevicesList.add(device.getName() + "\n" + device.getAddress());
			        }
			    }
				showBTDialogFragment();
	    	}
	    	else {
				AlertDialogFragment dialogNoBtSupport = AlertDialogFragment.newInstance(R.string.title_warning, "No s'ha pogut engegar el bluetooth");
				dialogNoBtSupport.show(getSupportFragmentManager(), "BtCouldntEnable");	
	    	}
	    }
	   
	}
		
	/**
	 * ActionBar - Menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Infla el menu. Aix� afegeix items a l'action bar.
		getMenuInflater().inflate(R.menu.activity_main, menu); //R.menu.activity_main --> /res/menu/activity_main.xml
		return true;
	}
	
	private void configureActionBar() {
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//Volem que al action bar es mostrin les tabs
	   
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	    		// Quan la tab es seleccionada, canviem de secci� al ViewPager.
	    		mViewPager.setCurrentItem(tab.getPosition());	        		        	
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	
	        }
	    };		
	    
		// Per a cada una de les seccions de l'aplicaci�, s'afegeix un tab a l'action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Crea un tab amb el text corresponent al titol definit per l'adapter.
			// Tamb� especifica el TabListener per quan el Tab es seleccionat.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					//.setTabListener(this));
					.setTabListener(tabListener));
		}
	}
	
	private void configureViewPager() {
		
		// Posar el ViewPager amb les seccions de l'adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter); //li configurem el adapter al viewpager
		
		// Quan fem swipe entre diferents seccions, seleccionem la corresponent 
		// tab.
		mViewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});	
		mViewPager.setCurrentItem(1);
	}
	
	public void close_application() {
		finish();
	}
	
	/** SENSORS *******************************************************************/
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{
		Log.v("HOVI-ONE", "values: " + event.values[0] + " " + event.values[1] + " " + event.values[2]);
	}

	//*****************************BLUETOOTH**************************************************
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			//Quan el discovery troba un dispositiu
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Agafa l'objecte BluetoothDevice del Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// Agafa el nom i l'addre�a i el posa en un array adapter per mostrar-lo en un ListView
					foundDevicesList.add(device.getName() + "\n" + device.getAddress());
					android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("bluetooth_list_fragment");
					((BTDialogFragment) prev).notifyListHasToUpdate();
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Aturar la progress bar
				foundDevicesList.add("Fi de discovery");
				android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("bluetooth_list_fragment");
				((BTDialogFragment) prev).notifyListHasToUpdate();				   
                
            }			
		}
	};
	
	public void beginDiscovery() {
		bluetoothManager.performDiscovery();
	}
	
	public BroadcastReceiver getDiscoveryReceiver() {
		return mReceiver;
	}
	
	public ArrayList<String> getFoundDevices() {
		return foundDevicesList;
	}
	public void pressed_Ok_EnableBluetooth() {
		bluetoothManager.enableBluetooth();	
	}
	
	
	public void showBTDialogFragment() {
		if (mStackLevel == 0) {
			++mStackLevel;
			android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment prev = getSupportFragmentManager().findFragmentByTag("bluetooth_list_fragment");
			if (prev != null) {
				ft.remove(prev);
			}
			
			ft.addToBackStack(null);
						
			BTDialogFragment deviceListFragment = BTDialogFragment.newInstance(mStackLevel);
			deviceListFragment.show(ft, "bluetooth_list_fragment");
		}
	}
	
	public void notShowBTDialogFragment() {
		--mStackLevel;
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();		
		Fragment prev = getSupportFragmentManager().findFragmentByTag("bluetooth_list_fragment");
		if (prev != null) {
			ft.remove(prev);
			getSupportFragmentManager().popBackStack();//no sabem l'ordre
			ft.commit();
		}		
	}
	
	public void crearThreadConnexio(BluetoothDevice remoteDevice) {
		mDeviceCon = remoteDevice;
		Conthread = new BTClientThread(remoteDevice, mHandler);
		Conthread.start();
		primeraVegada = false;
		Manthread = new BTManagingConThread(Conthread.returnSocket(), mHandler);
		Manthread.start();		
	}
	
	public void enviarStringBT(String s) {
		Manthread.write(s.getBytes());
	}
	
	public void enviarByteBT(byte[] b) {
		Manthread.write(b);
	}
	
	public void activaDesactivaBotoWraperMain(boolean on) {
		mSectionsPagerAdapter.activaDesactivaBotoWraper(on);
	}
}