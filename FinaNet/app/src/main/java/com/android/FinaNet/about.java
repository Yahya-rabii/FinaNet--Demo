package com.android.FinaNet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.vansuita.materialabout.views.AboutView;
import com.vansuita.materialabout.builder.AboutBuilder;

public class about extends AppCompatActivity {

    //BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

/*
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.about);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.about:

                        return true;

                    case R.id.suppro:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(), OrderSucceessActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nospro:
                        startActivity(new Intent(getApplicationContext(), MaActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    default:
                        return false;
                }
            }
        });

*/

        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.drawable.profile)
                .setCover(R.drawable.banner)
                .setName("Yahya Rabii")
                .setSubTitle("software engineering student")
                .setBrief("I am a Junior dev guy , studying at EMSI school . I like to Code, Design, Innovate and Experiment. I am an enthusiastic and a social person who loves to take up new challenges and learn new skills. I love meeting new people, exchanging ideas and spreading knowledge and positivity.")                .setAppIcon(R.mipmap.ic_launcher)
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGitHubLink("Yahya-rabii")
                .addFacebookLink("https://www.facebook.com/profile.php?id=100017547668031")
                .addTwitterLink("_NOXIDEUS_")
                .addInstagramLink("yahya____rabii")
                .addEmailLink("rabiiyahya1@gmail.com")
                //.addWhatsAppDirectChat( "554799650629")
                //.addWhatsappLink("Jr", "+554799650629")
                .addWebsiteLink("https://yahya-rabii.github.io/")
                .addFiveStarsAction()
                //.addMoreFromMeAction("https://yahya-rabii.github.io/")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                //.addUpdateAction()
                .setActionsColumnsCount(2)
                .addFeedbackAction("rabiiyahya1@gmail.com")
                .addPrivacyPolicyAction("https://yahya-rabii.github.io/thanks.html")
                .addIntroduceAction((Intent) null)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

         addContentView(view,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                         RelativeLayout.LayoutParams.WRAP_CONTENT));
         //setContentView(R.layout.activity_about);


    }
}