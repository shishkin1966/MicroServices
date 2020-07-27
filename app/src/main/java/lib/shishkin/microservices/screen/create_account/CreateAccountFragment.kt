package lib.shishkin.microservices.screen.create_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.jaredrummler.materialspinner.MaterialSpinner
import lib.shishkin.common.RippleTextView
import lib.shishkin.common.toDoubleValue
import lib.shishkin.microservices.ApplicationSingleton
import lib.shishkin.microservices.R
import lib.shishkin.microservices.action.CreateAccountTransaction
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Currency
import lib.shishkin.microservices.observe.EditTextDecimalObservable
import lib.shishkin.microservices.observe.EditTextLongObservable
import lib.shishkin.microservices.observe.EditTextObservable
import lib.shishkin.sl.action.IAction
import lib.shishkin.sl.action.handler.FragmentActionHandler
import lib.shishkin.sl.model.IModel
import lib.shishkin.sl.ui.AbsContentFragment
import java.util.*


class CreateAccountFragment : AbsContentFragment(), MaterialSpinner.OnItemSelectedListener<String>,
    Observer {
    companion object {
        const val NAME = "CreateAccountFragment"

        fun newInstance(): CreateAccountFragment {
            return CreateAccountFragment()
        }
    }

    private val actionHandler = FragmentActionHandler(this)
    private lateinit var spinner: MaterialSpinner
    private lateinit var friendlyNameView: EditText
    private lateinit var balanceValueView: EditText
    private lateinit var openAccountButton: RippleTextView
    private lateinit var friendlyNameObservable: EditTextObservable
    private var balanceDecimalObservable: EditTextDecimalObservable? = null
    private var balanceLongObservable: EditTextLongObservable? = null


    override fun createModel(): IModel {
        return CreateAccountModel(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = view.findViewById(R.id.spinner)
        spinner.setItems(
            listOf(
                Currency.RUR,
                Currency.USD,
                Currency.EUR,
                Currency.GBP,
                Currency.CHF
            )
        )
        spinner.selectedIndex = 0
        spinner.setOnItemSelectedListener(this)

        openAccountButton = view.findViewById(R.id.openAccountButton)
        openAccountButton.setOnClickListener(this::onClick)

        friendlyNameView = view.findViewById(R.id.friendlyNameView)
        balanceValueView = view.findViewById(R.id.balanceValueView)

        friendlyNameObservable = EditTextObservable(this, friendlyNameView)
        balanceLongObservable = EditTextLongObservable(this, balanceValueView)
    }

    override fun onAction(action: IAction): Boolean {
        if (!isValid()) return false

        if (actionHandler.onAction(action)) return true

        ApplicationSingleton.instance.onError(
            getName(),
            "Unknown action:$action",
            true
        )
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_account, container, false)
    }

    override fun onItemSelected(view: MaterialSpinner?, position: Int, id: Long, item: String?) {
        finishObservable()
        if (Currency.RUR == item) {
            balanceLongObservable = EditTextLongObservable(this, balanceValueView)
        } else {
            balanceDecimalObservable = EditTextDecimalObservable(this, balanceValueView)
        }
        balanceValueView.setText(balanceValueView.text.toString())
        balanceValueView.setSelection(balanceValueView.text.toString().length)
    }

    private fun onClick(v: View?) {
        openAccountButton.isEnabled = false

        val account = Account()
        account.friendlyName = friendlyNameView.text.toString().trim()
        account.balance = balanceValueView.text.toString().toDoubleValue()
        account.currency = spinner.text.toString()
        getModel<CreateAccountModel>().getPresenter<CreateAccountPresenter>().addAction(
            CreateAccountTransaction(account)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        friendlyNameObservable.finish()
        finishObservable()
    }

    private fun finishObservable() {
        if (balanceLongObservable != null) {
            balanceLongObservable?.finish()
            balanceLongObservable = null
        }
        if (balanceDecimalObservable != null) {
            balanceDecimalObservable?.finish()
            balanceDecimalObservable = null
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        refreshViews()
    }

    private fun refreshViews() {
        if (!friendlyNameView.text.isNullOrEmpty() && !balanceValueView.text.isNullOrEmpty()) {
            openAccountButton.isEnabled = balanceValueView.text.toString().toDoubleValue() > 0
        } else {
            openAccountButton.isEnabled = false
        }
    }

    override fun getName(): String {
        return NAME
    }

}

