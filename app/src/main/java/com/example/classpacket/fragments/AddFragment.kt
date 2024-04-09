package com.example.classpacket.fragments


import android.app.DatePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.classpacket.R
import com.example.classpacket.database.Phishing
import com.example.classpacket.database.ViewModel
import com.example.classpacket.databinding.FragmentAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class AddFragment : Fragment() {
    private lateinit var packetViewModel: ViewModel
    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        binding = FragmentAddBinding.bind(view)

        val classification = resources.getStringArray(R.array.classifications)
        packetViewModel = ViewModelProvider(this)[ViewModel::class.java]

        binding.captureBtn.setOnClickListener {
            val phishingLink = binding.originInp.text.toString()
            if (phishingLink.isNotEmpty()) {
                APICallTask(phishingLink).execute()
            } else {
                Toast.makeText(context, "Phishing link cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addCancelBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val datePicker = binding.dateInp
        datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedMonth/$selectedDay/$selectedYear"
                    datePicker.setText(selectedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        return binding.root
    }

    inner class APICallTask(private val phishingLink: String) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var response = ""
            try {
                val url = URL("https://developers.bolster.ai/api/neo/scan")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                val apiKey = "scr4bsgy34snc3u7ao9vyoslgasl6sv73pb1i28pwrbbkeaquzubrwqr2hjtmlrm"
                val urlInfo = JSONObject().put("url", phishingLink)
                val requestBody = JSONObject().apply {
                    put("apiKey", apiKey)
                    put("urlInfo", urlInfo)
                    put("scanType", "quick")
                }.toString()

                connection.doOutput = true
                val wr = DataOutputStream(connection.outputStream)
                wr.writeBytes(requestBody)
                wr.flush()
                wr.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                    var inputLine: String?
                    val stringBuilder = StringBuilder()
                    while (inputStream.readLine().also { inputLine = it } != null) {
                        stringBuilder.append(inputLine)
                    }
                    inputStream.close()
                    response = stringBuilder.toString()
                } else {
                    response = "Error: $responseCode"
                }
            } catch (e: Exception) {
                Log.e("APICallTask", "Exception: ${e.message}")
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val jsonResponse = JSONObject(result)
                val jobId = jsonResponse.getString("jobID")
                Log.e("APICallTask", "jobID: $jobId")
                // Delay for 5 seconds before executing APICallStatusTask
                GlobalScope.launch(Dispatchers.Main) {
                    delay(3000) // Delay for 5 seconds
                    APICallStatusTask(phishingLink, jobId).execute()
                }
            } catch (e: Exception) {
                Log.e("APICallTask", "Exception: ${e.message}")
                Toast.makeText(context, "Error retrieving job ID!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    inner class APICallStatusTask(private val phishingLink: String, private val jobId: String) : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var response = ""
            try {
                val url = URL("https://developers.bolster.ai/api/neo/scan/status")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")

                val apiKey = "scr4bsgy34snc3u7ao9vyoslgasl6sv73pb1i28pwrbbkeaquzubrwqr2hjtmlrm"
                val requestBody = JSONObject().apply {
                    put("apiKey", apiKey)
                    put("jobID", jobId)
                    put("disposition", true)
                }.toString()

                connection.doOutput = true
                val wr = DataOutputStream(connection.outputStream)
                wr.writeBytes(requestBody)
                wr.flush()
                wr.close()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                    var inputLine: String?
                    val stringBuilder = StringBuilder()
                    while (inputStream.readLine().also { inputLine = it } != null) {
                        stringBuilder.append(inputLine)
                    }
                    inputStream.close()
                    response = stringBuilder.toString()
                } else {
                    response = "Error: $responseCode"
                }
            } catch (e: Exception) {
                Log.e("APICallStatusTask", "Exception: ${e.message}")
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val jsonResponse = JSONObject(result)
                val disposition = jsonResponse.getString("disposition")
                // Run on the UI thread to show the Toast message
                activity?.runOnUiThread {
                    insertDataToDatabase(phishingLink, disposition)
                }
            } catch (e: Exception) {
                Log.e("APICallStatusTask", "Exception: ${e.message}")
                // Run on the UI thread to show the Toast message
                activity?.runOnUiThread {
                    Toast.makeText(context, "Error retrieving status!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun insertDataToDatabase(phishingLink: String, disposition: String) {
        val sharedPreferences = requireContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("user", null).toString()

        // Add item to database using ViewModel
        val newLink = Phishing().apply {
            this.link = phishingLink
            this.username = userName
            this.classification = disposition
        }
        packetViewModel.addItem(newLink)
        Toast.makeText(context, "Successfully Added!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.homeFragment)
    }
}
