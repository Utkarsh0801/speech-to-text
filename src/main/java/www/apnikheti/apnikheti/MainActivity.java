package www.apnikheti.apnikheti;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    ImageView imageView;

    static final Integer RecordAudioRequestcode = 1;
    private SpeechRecognizer speechRecognizer;
    AlertDialog.Builder alertSpeechDialog;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();

        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent sppechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sppechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        sppechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                ViewGroup viewGroup=findViewById(android.R.id.content);
                View dialogView= getLayoutInflater().from(MainActivity.this).inflate(R.layout.alertcustom,viewGroup,false);

        alertSpeechDialog=new AlertDialog.Builder(MainActivity.this);
        alertSpeechDialog.setMessage("listening......");
        alertSpeechDialog.setView(dialogView);
        alertDialog=alertSpeechDialog.create();
        alertDialog.show();



            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                imageView.setImageResource(R.drawable.ic_baseline_mic_24);
                ArrayList<String> arrayList=bundle.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                editText.setText(arrayList.get(0));
                alertDialog.dismiss();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    imageView.setImageResource(R.drawable.ic_baseline_mic_24);
                    speechRecognizer.startListening(sppechIntent);
                }
                return false;
            }

        });

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}
                    , RecordAudioRequestcode);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==RecordAudioRequestcode && grantResults.length>0)
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
        Toast.makeText(this,"permission Granted",Toast.LENGTH_LONG).show();
    }

}
}


