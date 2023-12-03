package id.xxx.module.auth.fragment.listener

import id.xxx.module.auth.model.SecurityChallengeResult

interface ISecurityChallengeFragment {

    fun onResult(result: SecurityChallengeResult)
}