json.array!(@city_organs) do |city_organ|
  json.extract! city_organ, :id, :cnpj, :cityName, :stateName, :regionName, :organName
  json.url city_organ_url(city_organ, format: :json)
end
