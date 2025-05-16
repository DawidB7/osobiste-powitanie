package com.example.osobistepowitanie;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID="ID";

    private EditText editTextimie;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextimie = findViewById(R.id.editTextImie);
        button = findViewById(R.id.buttonPowitanie);


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        CreateNotificationChannel();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imie = editTextimie.getText().toString().trim();

                if (imie.isEmpty()) {
                    builder.setTitle("Błąd");
                    builder.setMessage("Proszę wpisać swoje imię!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                } else {
                    builder1.setTitle("Potwierdzenie");
                    builder1.setMessage("Cześć "+imie+"! Czy chcesz otrzymac powiadomienia powitalne?");
                    builder1.setPositiveButton("Tak, poproszę", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Notification();
                        }
                    });
                    builder1.setNegativeButton("Nie, dziękuję", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Rozumiem. Nie wysyłam powiadomienia", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder1.show();
                }
            }
        });

    }
    private void CreateNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "kanal_powiadomien", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("opis_kanalu");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void Notification(){
        String imiePowiadomienie = editTextimie.getText().toString().trim();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Witaj!")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Miło cię widzieć, " + imiePowiadomienie + "!"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, builder.build());
        }
    }
}