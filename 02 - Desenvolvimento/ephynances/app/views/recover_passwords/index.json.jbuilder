json.array!(@recover_passwords) do |recover_password|
  json.extract! recover_password, :id, :token, :validity
  json.url recover_password_url(recover_password, format: :json)
end
