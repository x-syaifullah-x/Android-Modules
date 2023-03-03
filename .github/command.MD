#### DELETE WORKFLOW
```
gh api repos/${user}/${repo}/actions/runs/${id} -X DELETE
```
##### see ID:
```
gh run list
```
##### example:
```
gh api repos/x-syaifullah-x/android-modules/actions/runs/4377752138 -X DELETE
```