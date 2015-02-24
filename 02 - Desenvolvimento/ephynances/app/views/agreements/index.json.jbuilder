json.array!(@agreements) do |agreement|
  json.extract! agreement, :id, :type, :contactEmail, :contactPhone, :totalPrice, :contractorAgreementNumber, :physisAgreementNumber, :expiryDate, :status, :cnpjAmount
  json.url agreement_url(agreement, format: :json)
end
