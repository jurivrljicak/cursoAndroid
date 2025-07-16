package com.example.cursoandroidclase2.uid.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cursoandroidclase2.R
import com.example.cursoandroidclase2.viewmodel.PostViewModel
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursoandroidclase2.R.id.snackbar_text
import com.example.cursoandroidclase2.data.db.DatabaseProvider
import com.example.cursoandroidclase2.data.db.Post
import com.example.cursoandroidclase2.util.RemoteConfigManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.launch

class MainActivityPost : AppCompatActivity() {

    private val TAG = MainActivityPost :: class.java.simpleName
    private val viewModel: PostViewModel by viewModels() // ✅ esta es la correcta

    private lateinit var adapter: PostAdapter

    private var fireBaseOn = false

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val id = result.data?.getIntExtra("id", -1) ?: -1
            val title = result.data?.getStringExtra("title")
            val body = result.data?.getStringExtra("body")
            //if (!title.isNullOrBlank() && !body.isNullOrBlank()) {
                // val nuevoPost = Post(userId = 999, id = System.currentTimeMillis().toInt(), title = title, body = body)
                // val nuevaLista = adapter.currentList.toMutableList().apply { add(nuevoPost) }
                // adapter.submitList(nuevaLista)

            //}
            if (id != -1 && !title.isNullOrBlank() && !body.isNullOrBlank()) {
                val nuevaLista = adapter.currentList.toMutableList()
                val index = nuevaLista.indexOfFirst { it.id == id }
                if (index != -1) {
                    nuevaLista[index] = nuevaLista[index].copy(title = title, body = body)
                    adapter.submitList(nuevaLista)

                    val nuevoPost = Post(id = id, userId = 1, title = title, body = body)
                    lifecycleScope.launch {
                        DatabaseProvider.db.postDao().insert(nuevoPost)
                        val posts = DatabaseProvider.db.postDao().getAll()
                        posts.forEach { post ->
                            Log.d("PostDebug", "Post: id=${post.id}, title=${post.title}, body=${post.body}")
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseProvider.init(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 🎛️ RecyclerView + Adapter
        //adapter = PostAdapter()
//        adapter = PostAdapter { postToDelete ->
//            val currentList = adapter.currentList.toMutableList()
//            currentList.remove(postToDelete)
//            adapter.submitList(currentList)
//        }
        adapter = PostAdapter(
            onDeleteClick = { post ->
                //throw RuntimeException("Test Crash") // Force a crash
             //   FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
              //  FirebaseCrashlytics.getInstance().log("Mensaje desde Lomas de Zamora")


                Firebase.crashlytics.log("Entré a MainActivity")
                Firebase.crashlytics.setUserId("juri")
                Firebase.crashlytics.recordException(Exception("Error manual de juri"))


                val nuevaLista = adapter.currentList.toMutableList().apply { remove(post) }
                adapter.submitList(nuevaLista)
            },
            onEditClick = { post ->
                val intent = Intent(this, InputActivity::class.java).apply {
                    putExtra("id", post.id)
                    putExtra("title", post.title)
                    putExtra("body", post.body)
                }
                launcher.launch(intent)
            }
        )

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivityPost)
            adapter = this@MainActivityPost.adapter
        }

        // 👀 Observar cambios del ViewModel
        viewModel.posts.observe(this) {
            Log.d( "***** MainActivityPost", "Recibí ${it.size} posts")
            adapter.submitList(it)
        }
        viewModel.error.observe(this) { message ->
            /* val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_INDEFINITE)
            snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.my_snackbar_background))
            snackbar.setTextColor(ContextCompat.getColor(this, R.color.my_snackbar_text_color))
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.my_snackbar_text_action))
            snackbar.setAction("Cerrar"){
            }.show() */

            // Manipulacion por funcion de ambito
            val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_INDEFINITE)
            
            // se accede a la view interna del snackbar para setear atributos
           /* snackbar.view.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.my_snackbar_background))
                val textView = findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.setTextColor(Color.YELLOW)
                textView.textSize = 24f
                textView.maxLines = 5  // Permite más líneas
                textView.setTypeface(null, Typeface.BOLD)
            }
            snackbar.setAction("Cerrar"){
            }.show() */

           // como en el snackbar no tengo acceso a la view, se accede luego a la view para setear el tamaño
      /*      snackbar.apply {
                setBackgroundTint(ContextCompat.getColor(context, R.color.my_snackbar_background))
                setTextColor(ContextCompat.getColor(context, R.color.my_snackbar_text_color))
                setActionTextColor(ContextCompat.getColor(context, R.color.my_snackbar_text_action))
                view.apply {
                    val textView = findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    textView.textSize = 24f
                }
            }
            snackbar.setAction("Cerrar"){
            }.show()*/

            val customView =  LayoutInflater.from(this)
                .inflate(R.layout.custom_snackbar, null)

            customView.findViewById<TextView>(R.id.snackbar_text)
            customView.findViewById<Button>(R.id.snackbar_button).apply {
                setOnClickListener {
                    Toast.makeText(context, "Reintentando...", Toast.LENGTH_LONG).show()
                }
            }


            val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

            snackbarLayout.setPadding(0, 0, 0, 0)
            snackbarLayout.removeAllViews()
            snackbarLayout.addView(customView)
            snackbar.show()

        }
        viewModel.loadPosts(this)

        FirebaseApp.initializeApp(this);
/*

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(5)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        val label = remoteConfig.getString("label")

        Log.v("main", "Levantamos remote config $label")

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val label2 = remoteConfig.getString("label2")
                    Log.v("main", "Levantamos remote config callback $label2")
                } else {
                    Log.e("RemoteConfig", "Fetch failed", task.exception)
                }
            }
*/

    }

    override fun onStart() {
        super.onStart()

        if(!fireBaseOn) {

            fireBaseOn = !fireBaseOn

            RemoteConfigManager.fetchAndActivate { success ->
                if (success) {
                    val label = RemoteConfigManager.getString("label")
                    //  val enabled = RemoteConfigManager.getBoolean("feature_enabled")
                    Log.d(TAG, "Mensaje: $label")
                } else {
                    Log.e(TAG, "Error al cargar Remote Config")
                }
            }


            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM", "Token FCM: $token")
                } else {
                    Log.w("FCM", "No se pudo obtener token", task.exception)
                }
            }
        }




    }

}