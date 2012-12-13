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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private static final int REQUEST_ENABLE_BT = 1; //constant per identificar peticio de engegar bluetooth
	private boolean bluetoothEnabled;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;//El ViewPager contindrà el contingut de les seccions.

	BluetoothAdapter mBluetoothAdapter;//Adapter del Bluetooth
	
	ArrayAdapter<String> myArrayAdapter;//TODO Implementar la llista de dispositius BT i la UI
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Posa en marxa l'ActionBar
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//Crea les TABS
		
		// Crea l'adapter que retornarà un fragment per cada una de les 3 
		// seccions primaries de l'app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Posar el ViewPager amb les seccions de l'adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Quan fem swipe entre diferents seccions, seleccionem la corresponent 
		// tab.
		mViewPager.setOnPageChangeListener(
				new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// Per a cada una de les seccions de l'aplicació, s'afegeix un tab a l'action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Crea un tab amb el text corresponent al titol definit per l'adapter.
			// També especifica el TabListener per quan el Tab es seleccionat.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		setUpBluetooth();		
		findRemoteDevices();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    if (requestCode == REQUEST_ENABLE_BT) {
	    	if (resultCode == RESULT_OK) bluetoothEnabled = true;
	    	else bluetoothEnabled = false;
	    }
	    if (data.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
	    	//no se si s'ha de posar aquesta funcio
	    }
	   
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        //this.unregisterReceiver(mReceiver);
    }
	
	private void setUpBluetooth() {
		//1r Revisar que el dispositiu te Bluetooth
		bluetoothEnabled = false;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter != null) { //el dispositiu soporta bluetooth
			//Engegar el Bluetooth
			if (!mBluetoothAdapter.isEnabled()) {
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
			//TODO Mostrar un missatge per pantalla dient que no suporta BT. Usar FragmentAlertDialog !!			
			
		}
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
	        this.registerReceiver(mReceiver, filter);
			
			/**
			 Notice that cancelDiscovery() is called before the connection is made. You should always do this before 
			 connecting and it is safe to call without actually checking whether it is running or not 
			 (but if you do want to check, call isDiscovering())
			 */
			mBluetoothAdapter.cancelDiscovery(); //?¿?¿?¿
			
			//TODO Revisar que funcioni aixi
			AcceptThread thread = new AcceptThread();
			thread.run();
		}
		else {
			//El BT no s'ha activat degut a un error (o l'usuari ha respos 'no')
			//TODO Completar la UI de forma que al no tenir el BT activat, no es pot fer res. (o el minim)
			int i = 0;
			i = 2*2*i;
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

	/**
	 * ActionBar - Tab
	 */
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// Quan la tab es seleccionada, canviem de secció al ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * FragmentPagerAdapter que retorna un fragment corresponent a 
	 * una de les sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase();
			case 1:
				return getString(R.string.title_section2).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		//TODO completar el DummySectionFragment de forma que s'instancii el fragment correcte per a cada Secció.
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
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
