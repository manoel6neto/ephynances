# encoding: UTF-8
class Payment < ActiveRecord::Base

  validates_presence_of :value, :paymentDate, :confirmationDate, :status

  has_one :agreement_installment
  has_one :sub_agreement_installment
  has_many :payment_documents
end
