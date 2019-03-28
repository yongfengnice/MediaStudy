package com.suyf.mediastudy;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.suyf.mediastudy.audio.AudioActivity;

public class MainActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void openAudioActivity(View view) {
    startActivity(new Intent(this, AudioActivity.class));
  }
}
