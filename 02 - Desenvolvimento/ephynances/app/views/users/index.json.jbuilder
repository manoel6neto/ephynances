json.array!(@users) do |user|
  json.extract! user, :id, :name, :email, :phone, :cellPhone, :login, :password, :status
  json.url user_url(user, format: :json)
end
