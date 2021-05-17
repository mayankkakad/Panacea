package com.example.panacea.ui.chat;

import android.net.InetAddresses;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.panacea.MainActivity;
import com.example.panacea.R;
import com.example.panacea.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import static android.content.Context.WIFI_SERVICE;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
    Button goChat,sendMessage;
    EditText nameText;
    static EditText message;
    TextView oppName;
    FirebaseFirestore db;
    static Vector<String> chatusers;
    static Vector<Pair> pairs;
    static String anonymous,aname;
    static int count=0;
    boolean written=false,read=false;
    boolean chatting=false;
    static String serverIp;
    static int serverPort;
    static String ip;
    static int port;
    static FragmentActivity fa;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        db=FirebaseFirestore.getInstance();
        chatusers=new Vector<String>();
        pairs=new Vector<Pair>();
        fa=getActivity();
        oppName=(TextView)root.findViewById(R.id.textView49);
        oppName.setVisibility(View.GONE);
        goChat=(Button)root.findViewById(R.id.button19);
        nameText=(EditText)root.findViewById(R.id.editTextTextPersonName);
        message=(EditText)root.findViewById(R.id.editTextTextPersonName4);
        sendMessage=(Button)root.findViewById(R.id.button20);
        message.setVisibility(View.GONE);
        sendMessage.setVisibility(View.GONE);
        goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chatting) {
                    if (nameText.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                    else {
                        oppName.setText("Pairing...");
                        oppName.setVisibility(View.VISIBLE);
                        nameText.setVisibility(View.GONE);
                        searchPeople();
                    }
                }
                else {
                    oppName.setVisibility(View.GONE);
                    nameText.setVisibility(View.VISIBLE);
                    goChat.setText("Chat");
                    chatting=false;
                    db.collection("chat").document(MainActivity.loggedemail).delete();
                    message.setVisibility(View.GONE);
                    sendMessage.setVisibility(View.GONE);
                }
            }
        });
        return root;
    }

    public void searchPeople()
    {
        db.collection("chat").document(MainActivity.loggedemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
                    Random r=new Random();
                    port=r.nextInt(16383)+49152;
                    Map<String,Object> data=new HashMap<>();
                    data.put("email",MainActivity.loggedemail);
                    data.put("name",nameText.getText().toString());
                    ip=Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                    try { data.put("ip",ip);}catch(Exception e){}
                    data.put("port",port);
                    DocumentSnapshot doc=task.getResult();
                    if(!doc.exists())
                    {
                        db.collection("chat").document(MainActivity.loggedemail).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    getUsersAndPair();
                            }
                        });
                    }
                    else
                    {
                        db.collection("chat").document(MainActivity.loggedemail).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    getUsersAndPair();
                            }
                        });
                    }
                }
            }
        });
    }

    public void getUsersAndPair()
    {
        db.collection("chat").document(MainActivity.loggedemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc=task.getResult();
                    if(doc.get("pair")==null) {
                        db.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    chatusers.clear();
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        if(document.get("pair")==null)
                                            chatusers.add(document.get("email").toString());
                                    }
                                    pairUsers();
                                }
                            }
                        });
                    }
                    else {
                        anonymous = doc.get("pair").toString();
                        pairUsers();
                    }
                }
            }
        });
    }

    public void pairUsers()
    {
        Random r=new Random();
        int temp=chatusers.size();
        if(temp%2==1) {
            chatusers.remove(r.nextInt(temp));
            temp--;
        }
        boolean status[]=new boolean[temp];
        temp=temp/2;
        pairs.clear();
        for(int i=0;i<temp;i++)
        {
            int t=r.nextInt((temp*2)-i)+i;
            if((status[t])||(i==t))
            {
                i--;
                continue;
            }
            status[i]=status[t]=true;
            pairs.add(new Pair(chatusers.get(i),chatusers.get(t)));
        }
        if(pairs.size()==0)
            setPage();
        count=0;
        for(int i=0;i<pairs.size();i++)
        {
            count++;
            if(pairs.get(i).email1.equals(MainActivity.loggedemail))
                anonymous=pairs.get(i).email2;
            else if(pairs.get(i).email2.equals(MainActivity.loggedemail))
                anonymous=pairs.get(i).email1;
            Map<String,Object> d=new HashMap<>();
            d.put("pair",pairs.get(i).email2);
            d.put("role","server");
            db.collection("chat").document(pairs.get(i).email1).update(d);
            d=new HashMap<>();
            d.put("pair",pairs.get(i).email1);
            d.put("role","client");
            db.collection("chat").document(pairs.get(i).email2).update(d).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        if(count==pairs.size())
                            setPage();
                    }
                }
            });
        }
    }

    public void setPage()
    {
        db.collection("chat").document(MainActivity.loggedemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.get("pair")==null)
                        anonymous=null;
                    else
                        anonymous=doc.get("pair").toString();
                    if(anonymous==null)
                    {
                        oppName.setVisibility(View.GONE);
                        nameText.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(),"No Users Available",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        db.collection("chat").document(anonymous).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot doc=task.getResult();
                                    aname=doc.get("name").toString();
                                    oppName.setText(aname);
                                    message.setVisibility(View.VISIBLE);
                                    sendMessage.setVisibility(View.VISIBLE);
                                    chatting=true;
                                    goChat.setText("Leave");
                                    setConnectionParameters();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void setConnectionParameters()
    {
        db.collection("chat").document(anonymous).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc=task.getResult();
                    serverIp=doc.get("ip").toString();
                    serverPort=Integer.parseInt(doc.get("port").toString());
                    setupConnection();
                }
            }
        });
    }

    public void setupConnection()
    {
        db.collection("chat").document(MainActivity.loggedemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc=task.getResult();
                    if(doc.get("role").toString().equals("server"))
                    {
                        Thread server=new Thread(new Server());
                        server.start();
                    }
                    else
                    {
                        Thread client=new Thread(new Client());
                        client.start();
                    }
                }
            }
        });
    }
}

class Server implements Runnable
{
    ServerSocket serverSocket;
    Socket socket;
    PrintWriter output;
    BufferedReader input;
    @Override
    public void run()
    {
        try {
            serverSocket = new ServerSocket(ChatFragment.port);
            socket=serverSocket.accept();
            output=new PrintWriter(socket.getOutputStream());
            input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Toast.makeText(ChatFragment.fa,"Connection Established",Toast.LENGTH_SHORT);
        }
        catch(Exception e){Toast.makeText(ChatFragment.fa,"Connection Failed",Toast.LENGTH_SHORT);}
    }
}

class Client implements Runnable
{
    Socket socket;
    PrintWriter output;
    BufferedReader input;
    @Override
    public void run()
    {
        try {
            socket=new Socket(ChatFragment.serverIp,ChatFragment.serverPort);
            output=new PrintWriter(socket.getOutputStream());
            input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Toast.makeText(ChatFragment.fa,"Connection Established",Toast.LENGTH_SHORT);
        }
        catch(Exception e){Toast.makeText(ChatFragment.fa,"Connection Failed",Toast.LENGTH_SHORT);}
    }
}

class Pair
{
    String email1,email2;
    public Pair(String email1,String email2)
    {
        this.email1=email1;
        this.email2=email2;
    }
}