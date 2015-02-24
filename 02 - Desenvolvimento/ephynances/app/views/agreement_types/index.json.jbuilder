json.array!(@agreement_types) do |agreement_type|
  json.extract! agreement_type, :id, :type
  json.url agreement_type_url(agreement_type, format: :json)
end
