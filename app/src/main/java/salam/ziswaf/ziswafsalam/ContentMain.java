package salam.ziswaf.ziswafsalam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ContentMain extends Fragment {
    TextView txtUser;
    String c_user,c_amilin="";
    public static ContentMain newInstance() {
        ContentMain fragment = new ContentMain();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.content_main, container, false);
        txtUser = (TextView) view.findViewById(R.id.txtUser);
        try
        {
            FileCacher<String> user = new FileCacher<String>(getActivity(), "datauser.txt");
            if (user.hasCache()) {
                c_user = user.readCache();
                JSONObject objUser=new JSONObject(c_user);
                JSONObject d_user = objUser.getJSONObject("data");
                c_amilin = d_user.getString("nama");
                txtUser.setText(c_amilin);
            }
        }
        catch (JSONException e)
        {
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
        catch (IOException e)
        {
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG
            );
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
        return view;
    }
}
