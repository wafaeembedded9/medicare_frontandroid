package com.ouafaa.medicare_connect;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QstAnswer extends AppCompatActivity {

    private TextView[] questions;
    private TextView[] answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qst_answer);

        // Initialisez les tableaux de questions et de réponses
        questions = new TextView[]{
                findViewById(R.id.questionDiabetes),
                findViewById(R.id.questionStress),
                findViewById(R.id.questionSommeil),
                findViewById(R.id.questionMove),
                findViewById(R.id.questionNutr),
                findViewById(R.id.questionSkin),
                findViewById(R.id.questionCrise),

        };
        answers = new TextView[]{
                findViewById(R.id.answerDiabetes),
                findViewById(R.id.answerStress),
                findViewById(R.id.answerSommeil),
                findViewById(R.id.answerMove),
                findViewById(R.id.answerNutr),
                findViewById(R.id.answerSkin),
                findViewById(R.id.answerCrise),

        };

        // Attachez un écouteur de clic à chaque question
        for (int i = 0; i < questions.length; i++) {
            final int index = i;
            questions[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleAnswer(index);
                }
            });
        }
    }

    private void toggleAnswer(int index) {
        // Afficher ou masquer la réponse correspondante
        if (index >= 0 && index < answers.length) {
            TextView answer = answers[index];
            if (answer.getVisibility() == View.VISIBLE) {
                answer.setVisibility(View.GONE);
            } else {
                answer.setVisibility(View.VISIBLE);
            }
        }
    }
}


