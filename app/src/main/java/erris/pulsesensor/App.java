package erris.pulsesensor;

import android.app.Application;
import android.content.Context;

import erris.pulsesensor.database.DBHelper;
import erris.pulsesensor.database.DatabaseManager;

public class App extends Application {
    private static Context context;
    private static DBHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new DBHelper(context);
        DatabaseManager.initializeInstance(dbHelper);
    }

    public static Context getContext() {
        return context;
    }
}
