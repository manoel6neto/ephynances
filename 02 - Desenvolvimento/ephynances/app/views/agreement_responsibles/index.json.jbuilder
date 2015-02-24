json.array!(@agreement_responsibles) do |agreement_responsible|
  json.extract! agreement_responsible, :id, :name, :email, :phone, :cellPhone, :cpf
  json.url agreement_responsible_url(agreement_responsible, format: :json)
end
