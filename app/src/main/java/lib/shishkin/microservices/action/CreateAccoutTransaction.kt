package lib.shishkin.microservices.action

import lib.shishkin.microservices.data.Account
import lib.shishkin.sl.action.IAction

class CreateAccountTransaction(val account: Account) : IAction
