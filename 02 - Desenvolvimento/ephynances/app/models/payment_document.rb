class PaymentDocument < ActiveRecord::Base

  validates_presence_of :name, :file, :extension, :description, :size
  validates_length_of :description, maximum: 500, allow_blank: false

  belongs_to :payment
end
