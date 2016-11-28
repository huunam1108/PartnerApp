package com.example.app2;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;
import com.example.utils.PkgCertWhiteLists;
import java.util.ArrayList;

public class PartnerActivity extends AppCompatActivity {
    // shared provider
    public static final String SHARED_AUTHORITY = "com.example.app1.MyProvider";

    // shared interface
    public interface UserData {
        String PATH = "shareddata";
        Uri CONTENT_URI = Uri.parse("content://" + SHARED_AUTHORITY + "/" + PATH);
    }

    private ListView mLvData;
    private PartnerAdapter mAdapter;
    private ArrayList<String> mVersions = new ArrayList<>();

    private static PkgCertWhiteLists sWhiteLists = null;

    private static void buildWhiteLists(Context context) {
        boolean isDebug = BuildConfig.DEBUG;
        sWhiteLists = new PkgCertWhiteLists();
        // Register certificate hash value of provider application com.example.app1
        sWhiteLists.add("com.example.app1", isDebug ?
                // Certificate hash value of "androiddebugkey" in the debug.keystore.
                "4A4DBC9A 526EA032 D3B2FD89 45D2E971 63A066B2 4481A751 116E07D1 98E7B148" :
                // Certificate hash value of "partner key" in the keystore.
                "1F039BB5 7861C27A 3916C778 8E78CE00 690B3974 3EB8259F E2627B8D 4C0EC35A");

        // Add other partner here via add(String pkgName, String sha256)
    }

    private static boolean checkPartner(Context context, String pkgName) {
        if (sWhiteLists == null) {
            buildWhiteLists(context);
        }
        if (context.getPackageName().equals(pkgName)) {
            // current app is in used
            return true;
        }
        return sWhiteLists.test(context, pkgName);
    }

    private String providerPkgname(Uri uri) {
        String pkgName = null;
        ProviderInfo pi = getPackageManager().resolveContentProvider(uri.getAuthority(), 0);
        if (pi != null) {
            pkgName = pi.packageName;
        }
        return pkgName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvData = (ListView) findViewById(R.id.listView);
        if (!checkPartner(this, providerPkgname(UserData.CONTENT_URI))) {
            Toast.makeText(this, "There is no public provider with this authority !",
                    Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Cursor cursor =
                    getContentResolver().query(UserData.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    Toast.makeText(this, "no data yet", Toast.LENGTH_SHORT).show();
                } else {
                    do {
                        String fullName = cursor.getString(cursor.getColumnIndex("full_name"));
                        mVersions.add(fullName);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter = new PartnerAdapter(this, mVersions);
        mLvData.setAdapter(mAdapter);
    }
}
