pub_key="$(cat ~/.ssh/id_rsa.pub)"
ssh_add_exit_code=0
ssh_add_output="$(ssh-add -L 2>&1)" || ssh_add_exit_code=$?

function require_ssh_keys {
  if [[ $ssh_add_exit_code -eq 2 ]]; then
    echo 'This command needs access to your SSH keys.' >&2
    echo >&2
    echo 'Please run the following command to start ssh-agent:' >&2
    echo >&2
    echo '    ssh-agent bash' >&2
    echo >&2
    echo 'Then add your keys to the agent:' >&2
    echo >&2
    echo '    ssh-add ~/.ssh/id_rsa' >&2
    echo >&2
    exit 1
  fi

  echo "$ssh_add_output" | grep "$(echo "$pub_key" | cut -d ' ' -f 2)" >/dev/null 2>&1 || {
    echo 'This command needs access to your SSH keys.' >&2
    echo >&2
    echo 'Please run the following command to add your SSH keys to ssh-agent:' >&2
    echo >&2
    echo '    ssh-add ~/.ssh/id_rsa' >&2
    echo >&2
    exit 1
  }
}
