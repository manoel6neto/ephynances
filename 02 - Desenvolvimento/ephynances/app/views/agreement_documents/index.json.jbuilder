json.array!(@agreement_documents) do |agreement_document|
  json.extract! agreement_document, :id
  json.url agreement_document_url(agreement_document, format: :json)
end
