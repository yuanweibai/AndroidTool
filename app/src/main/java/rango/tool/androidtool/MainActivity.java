package rango.tool.androidtool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import rango.kotlin.KotlinTestActivity;
import rango.kotlin.designmode.mvp.view.IpActivity;
import rango.kotlin.views.ViewActivity;
import rango.tool.androidtool.base.BaseActivity;
import rango.tool.androidtool.coordinator.CoordinatorActivity;
import rango.tool.androidtool.experiments.TestActivity;
import rango.tool.androidtool.experiments.activity.AnyThingActivity;
import rango.tool.androidtool.game.GameMainActivity;
import rango.tool.androidtool.launchmodel.LaunchMode1Activity;
import rango.tool.androidtool.list.activity.ListActivity;
import rango.tool.androidtool.list.activity.RecyclerActivity;
import rango.tool.androidtool.touch.TouchActivity;
import rango.tool.androidtool.transition.OffsetActivity;
import rango.tool.androidtool.transition.TransitionActivity;
import rango.tool.androidtool.util.CryptoUtils;

public class MainActivity extends BaseActivity {

    private LinearLayout containerView;

    private static final boolean IS_TEST = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (IS_TEST) {
            startActivity(AnyThingActivity.class);
        }

        setContentView(R.layout.activity_main);
//        WindowUtil.hideStatusBar(this);

        containerView = findViewById(R.id.container_view);

        findViewById(R.id.recycler_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.list_btn).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.transition_btn).setOnClickListener(v -> startActivity(TransitionActivity.class));
        findViewById(R.id.coordinator_btn).setOnClickListener(v -> startActivity(CoordinatorActivity.class));
        findViewById(R.id.test_btn).setOnClickListener(v -> {
            startActivity(TestActivity.class);
        });

        findViewById(R.id.add_view_btn).setOnClickListener(v -> addTestView());
        findViewById(R.id.offset_btn).setOnClickListener(v -> startActivity(OffsetActivity.class));
        findViewById(R.id.touch_btn).setOnClickListener(v -> startActivity(TouchActivity.class));
        findViewById(R.id.game_btn).setOnClickListener(v -> startActivity(GameMainActivity.class));

        String string = "spyIFKsRDDywx96YXDjAyiuo0imMzoGBOIhavrlmvma5A0gXJTzklg/jrGbVUd11VqigDk7zJCQt\n" +
                "p7wEnVs3pnn5rU7WsT+F+yTJhe9zcLM7l84rL+/TO4qVN+hBS6mson6KhsiZXV/b06jpcbKanAxe\n" +
                "FZbO9qUldlJUUC/aG2dA5IMKwvuBfyZA8kimNmpvaLOxpWEd0iAxbslecjKiTV86SSn0aBwN4T/A\n" +
                "Qm6uykGdLKG/2B4sVRq7fFzUSsROKIZUMr9BwwLwOUSiA7yQubA5o/EELjHeoy6YIg8S+e5ifjSi\n" +
                "y42AUATZKpyz9v9KFf4ZR2hHxGHUp5b75FLtAii7F8CIq5Bd384VEXyGSlrRaHNeCrbtlWzBozHq\n" +
                "QAugtaJnOtijIpUJFU4wEiy3rVJpXUAZd58Va7MzcJzsOyRn1lAK7PQvKShFbI91WkQ8NYglST3+\n" +
                "OoPcMfEbXRnKh8Jbnf39Fr9+MqtFUbFp4OwPLmiHEapPXt+esv7tHUup\n";

        Log.e("rango", CryptoUtils.decrypt(string));

    }

    private void addTestView() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.solon);
        containerView.addView(imageView, 1);
    }
}
