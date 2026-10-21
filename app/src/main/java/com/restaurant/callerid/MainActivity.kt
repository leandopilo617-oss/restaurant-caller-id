import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etPhone: EditText
    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var etOrder: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPhone = findViewById(R.id.etPhone)
        etName = findViewById(R.id.etName)
        etAddress = findViewById(R.id.etAddress)
        etOrder = findViewById(R.id.etOrder)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // طلب الصلاحيات عند فتح التطبيق أول مرة
        checkPermissions()

        // استقبال الرقم إذا تم الاتصال والتطبيق مفتوح أو بالخلفية
        handleIntent(intent)

        btnSave.setOnClickListener {
            val phone = etPhone.text.toString()
            val name = etName.text.toString()
            val order = etOrder.text.toString()
            
            if (phone.isNotEmpty()) {
                Toast.makeText(this, "تم حفظ الطلب بنجاح!", Toast.LENGTH_SHORT).show();
                // هنا يمكنك ربطه بقاعدة بيانات محلية أو طباعة مباشرة
                clearFields()
            } else {
                Toast.makeText(this, "رقم الهاتف مطلوب", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        val number = intent.getStringExtra("CALLER_NUMBER")
        if (!number.isNullOrEmpty()) {
            etPhone.setText(number)
            // تنبيه صوتي أو بصري خفيف للموظف
            Toast.makeText(this, "اتصال وارد من: $number", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.ANSWER_PHONE_CALLS
        )
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 100)
        }
    }

    private fun clearFields() {
        etPhone.setText("")
        etName.setText("")
        etAddress.setText("")
        etOrder.setText("")
    }
}
