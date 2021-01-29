package ir.sinadalvand.projects.bazaarproject.ui

import android.os.Bundle
import ir.sinadalvand.projects.bazaarproject.R
import ir.sinadalvand.projects.bazaarproject.contracts.Xactivity

class MainActivity : Xactivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}