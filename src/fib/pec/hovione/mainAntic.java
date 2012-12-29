package fib.pec.hovione;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class mainAntic extends FragmentActivity { // public class mainANtic extends FragmentActivity
	
	//////// Constants De La Classe ////////////////////////////////////////////////////////////////////////////////////////////	
	private static final int REQUEST_ENABLE_BT = 1; //constant per identificar peticio de engegar bluetooth
	
	/////// Atributs De La Classe /////////////////////////////////////////////////////////////////////////////////	
	private boolean bluetoothEnabled;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	
	private ViewPager mViewPager;//El ViewPager contindrà el contingut de les seccions. ViewPager es un tipus de layout que permet
                         // desplasarnos entre pantalles(tambe anomenades pagines o seccions) lliscant el dit horitzontalment. Al viewpager
	                     // se li ha de configurar un pagerAdapter que s'encarrega de crear les pagines que es mostraràn.
	                     // Normalment les pagines del viewpager son fragments i es fa servir un adapter que tracta
	                     // les pagines com a fragments anomenat FragmentPageAdapter.
	
	private SectionsPagerAdapter mSectionsPagerAdapter; //FragmentPageAdapter que gestiona la creacio de fragments(pagines) per al viewpager.

	private BluetoothAdapter mBluetoothAdapter;//Adapter del Bluetooth
	
	private ArrayAdapter<String> myArrayAdapter;//TODO Implementar la llista de dispositius BT i la UI
	
	private int mStackLevel = 0;
	
	private ActionBar actionBar;
	
	
	/////// Metodes /////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//inicialitzacions de variables
		actionBar = getActionBar();
		mViewPager = (ViewPager) findViewById(R.id.pager); //obtenim el viewpager del arxiu de layout xml de aquesta activity
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
					
		configureActionBar();
		configureViewPager();
		
		//myArrayAdapter = new ArrayAdapter<String>();
		setBTDialogFragment();
		/*
		setUpBluetooth();
		if (mBluetoothAdapter != null) { //necessari aquest if aqui ja que encara que mostrem dialog quan BT no està suportat, la execucio continúa igualment.
		    findRemoteDevices();
		    //setBTDialogFragment();
		}
		*/
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mReceiver);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == REQUEST_ENABLE_BT) {
	    	if (resultCode == RESULT_OK) {
	    		bluetoothEnabled = true;
	    	}
	    	else {
	    		bluetoothEnabled = false;
	    	}
	    }
	    if (data.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
	    	//no se si s'ha de posar aquesta funcio
	    }
	   
	}
		
	/**
	 * ActionBar - Menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Infla el menu. Això afegeix items a l'action bar.
		getMenuInflater().inflate(R.menu.activity_main, menu); //R.menu.activity_main --> /res/menu/activity_main.xml
		return true;
	}
	
	
	
	private void configureActionBar() {
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//Volem que al action bar es mostrin les tabs
	   
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	    		// Quan la tab es seleccionada, canviem de secció al ViewPager.
	    		mViewPager.setCurrentItem(tab.getPosition());	        		        	
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	        	
	        }
	    };		
	    
		// Per a cada una de les seccions de l'aplicació, s'afegeix un tab a l'action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Crea un tab amb el text corresponent al titol definit per l'adapter.
			// També especifica el TabListener per quan el Tab es seleccionat.
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
	}
	
	public void close_application() {
		finish();
	}

	//*****************************BLUETOOTH**************************************************
	private void setUpBluetooth() {
		//1r Revisar que el dispositiu te Bluetooth
		bluetoothEnabled = false;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter != null) { //el dispositiu soporta bluetooth
			//Engegar el Bluetooth
			if (!mBluetoothAdapter.isEnabled()) {
				AlertDialogFragment dialogBtNoEnabled = AlertDialogFragment.newInstance(R.string.title_bluetooth_enable_petition,
						                                                                "El bluetooth està desconnectat,\n" +
                                                                                        "vols que l'aplicació l'engegui per a tu?");
				dialogBtNoEnabled.show(getSupportFragmentManager(), "BtNoEnabled");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
			else {
				//El Bluetooth ja estava activat
				bluetoothEnabled = true;
			}		
		}
		else {
			//El dispositiu no suporta Bluetooth. Mostrar un missatge!		
			
			AlertDialogFragment dialogNoBtSupport = AlertDialogFragment.newInstance(R.string.title_warning, "Bluetooth No Soportat");
			dialogNoBtSupport.show(getSupportFragmentManager(), "BtNoSupported");
		}
	}
	
	public void Pressed_Ok_EnableBluetooth() {
		Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);		
	}
	
	
	private void findRemoteDevices() {
		if (bluetoothEnabled) {
			//Tenim el BT activat, procedim a buscar dispositius
			
			//1r Busquem entre la llista d'emparellats
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					//Aqui mostrar una llista dels BT emparellats per no haver de buscar-los
					myArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			}
			
			//TODO Completar aquesta secció de Bluetooth perque segur que esta malament...
			//2n Descobrim dispositius	
			mBluetoothAdapter.startDiscovery();
			// Registrar el BroadcastReciver
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(mReceiver, filter); //des-registrar al onDestroy
			
			// Register for broadcasts when discovery has finished
	        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	        registerReceiver(mReceiver, filter);
			
			/**
			 Notice that cancelDiscovery() is called before the connection is made. You should always do this before 
			 connecting and it is safe to call without actually checking whether it is running or not 
			 (but if you do want to check, call isDiscovering())
			 */
			mBluetoothAdapter.cancelDiscovery(); //?¿?¿?¿
			
			//TODO Revisar que funcioni aixi
			//AcceptThread thread = new AcceptThread();
			//thread.run();
		}
		else {
			//El BT no s'ha activat degut a un error (o l'usuari ha respos 'no')
			//TODO Completar la UI de forma que al no tenir el BT activat, no es pot fer res. (o el minim)
			
		}
	}
	
	public void setBTDialogFragment() {
		++mStackLevel;
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		
		DialogFragment newFragment = BTDialogFragment.newInstance(mStackLevel);
		newFragment.show(ft, "dialog");
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			//Quan el discovery troba un dispositiu
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Agafa l'objecte BluetoothDevice del Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					// Agafa el nom i l'addreça i el posa en un array adapter per mostrar-lo en un ListView
					myArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Aturar la progress bar
                
            }			
		}
	};
	
	private class AcceptThread extends Thread {
		private final UUID 	 MY_UUID = new UUID(0, 0);
		private final String NAME = "HOVI-ONE-Android";
		private final BluetoothServerSocket mmServerSocket;
		
		public AcceptThread() {
			BluetoothServerSocket tmp = null;
			try {
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmServerSocket = tmp;
		}
		
		public void run() {
			BluetoothSocket socket = null;
			// Continua escoltant fins que passa una excepció o es retorna un socket
			while (true) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
				// Si s'accepta una connexió
				if (socket != null) {
					//fer feina per manejar la connexió en un altre thread :D
					ConnectedThread thread = new ConnectedThread(socket);
					thread.run();
					
					try {
						mmServerSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		
		/**
		 * Cancelarà el socket, i causarà el final del thread
		 */
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}
		
		public void run() {
			byte[] buffer = new byte[1024]; //buffer per guardar el stream
			int bytes; //num bytes retornats pel read();
			
			// Es manté escoltant el stream fins que una excepcio passa
			while (true) {
				try {
					//Llegeix del InputStream
					bytes = mmInStream.read(buffer);
					// Envia els bytes obtinguts a la UI Activity
					
				} catch (IOException e) {
					break;
				}
			}
		}
		
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) { }
		}
		/**
		 * Cancelarà el socket, i causarà el final del thread
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
