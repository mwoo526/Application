package org.androidtown.application;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.Inflater;
//support.v4 -> 예전 버전까지 지원



public class DetailFragment extends Fragment {

    TextView textView1,textView2,textView3;
    ImageButton imageButton;
    private View container;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup=(ViewGroup) inflater.inflate(R.layout.fragment_detail,container,false);
        // param1 : xml 레이아웃 파일
        // param2 : xml 레이아웃이 설정될 뷰그룹 객체
        textView1=(TextView)viewGroup.findViewById(R.id.textView1);
        textView2=(TextView)viewGroup.findViewById(R.id.textView2);
        textView3=(TextView)viewGroup.findViewById(R.id.textView3);
        imageButton=(ImageButton)viewGroup.findViewById(R.id.imageButton);
        textView1.setText(((DetailActivity)getActivity()).getStoreaddress());
        textView2.setText(((DetailActivity)getActivity()).getStoretel());
        textView3.setText(((DetailActivity)getActivity()).getStoretime());

        Linkify.addLinks(textView1,Linkify.MAP_ADDRESSES);
        Linkify.addLinks(textView2,Linkify.PHONE_NUMBERS);

        ImageButton.OnClickListener clickListener=new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                container.buildDrawingCache();
                Bitmap captureView = container.getDrawingCache();

                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                if(!dir.exists()) dir.mkdir();
                String ad = dir.getAbsolutePath()+"" + "capture.jpeg";
                FileOutputStream fos;
                try{
                    fos = new FileOutputStream(ad);
                    captureView.compress(Bitmap.CompressFormat.JPEG,100,fos);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                Uri uri = Uri.fromFile(new File(ad));
                Intent intent= new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.setType("image/*");

                PackageManager packManager = getActivity().getPackageManager(); // mcontext
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for(ResolveInfo resolveInfo: resolvedInfoList) {
                    if(resolveInfo.activityInfo.packageName.startsWith("com.facebook.katana")){
                        intent.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name );
                        resolved = true;
                        break;
                    }
                }
                if(resolved) {
                    startActivity(Intent.createChooser(intent,"공유"));

                } else {
                    Toast.makeText(getActivity(), "페이스북 앱이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        };
        imageButton.setOnClickListener(clickListener);

        return  viewGroup;
    }



}
