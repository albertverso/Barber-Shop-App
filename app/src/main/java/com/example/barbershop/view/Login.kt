package com.example.barbershop.view

import android.annotation.SuppressLint
import android.app.LauncherActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.barbershop.LoginToRegisterActivity
import com.example.barbershop.MainActivity
import com.example.barbershop.R
import com.example.barbershop.SplashScreenActivity
import com.example.barbershop.models.User
import com.example.barbershop.models.ViewModelApp
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@SuppressLint("ResourceType")
class Login : Fragment(R.layout.fragment_login) {
    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("ResourceType", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = Color.parseColor("#2C3E50")

        val email = view.findViewById<EditText>(R.id.editEmail)
        val senha = view.findViewById<EditText>(R.id.editSenha)

        view.findViewById<Button>(R.id.btLogin).setOnClickListener {
           Submit(view, email, senha)
        }

        view.findViewById<TextView>(R.id.btRegister).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
            setStatusBarColor(color = Color.parseColor("#E74C3C"))
        }

        view.findViewById<TextView>(R.id.editSenha).setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                Submit(view, email, senha)
                return false
            }
        })
    }

    private fun Submit(it:View, email: EditText, senha: EditText) {
        val intent = Intent(activity, SplashScreenActivity::class.java)
        when{
            email.length() < 1 -> {
                mensagem(it, "Preencha seu email!")
            }
            senha.length() < 1 -> {
                mensagem(it, "Preencha a senha!")
            }
            else -> {
                auth.signInWithEmailAndPassword(email.text.toString(), senha.text.toString()).addOnCompleteListener { autenticacao ->
                    if(autenticacao.isSuccessful){
                        startActivity(intent)
                        activity?.finish()
                    }
                }.addOnFailureListener {
                    mensagem(requireView(), "Erro ao fazer o login!")
                }
            }
        }
    }

    private fun mensagem(view : View, mensagem : String) {
        val snackbar = Snackbar.make(view,mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun setStatusBarColor(color: Int) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = color
    }
}
