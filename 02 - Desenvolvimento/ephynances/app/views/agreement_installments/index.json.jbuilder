json.array!(@agreement_installments) do |agreement_installment|
  json.extract! agreement_installment, :id, :value, :status, :paymentDate, :dueDate, :confirmationDate
  json.url agreement_installment_url(agreement_installment, format: :json)
end
